/*
 *
 * (C) Copyright ${year} Ján Srniček (https://github.com/Marssmart)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.deer.dev.manager.persist.xodus;

import static java.lang.String.format;
import static java.nio.ByteBuffer.wrap;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

import java.io.ByteArrayOutputStream;
import java.util.Optional;
import jetbrains.exodus.ByteBufferByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.ByteIterator;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import org.deer.dev.manager.persist.api.DataCodec;
import org.deer.dev.manager.persist.api.DataCodecRegistry;
import org.deer.dev.manager.persist.api.Identifiable;
import org.deer.dev.manager.persist.api.PersistenceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class XodusPersistenceAdapter implements PersistenceAdapter {

  private static final Logger LOG = LoggerFactory.getLogger(XodusPersistenceAdapter.class);

  private final Environment environment;
  private final Store store;
  private final DataCodecRegistry codecRegistry;

  XodusPersistenceAdapter(final DataCodecRegistry codecRegistry,
      final Environment environment,
      final String storeName,
      final StoreConfig storeConfig) {
    this.codecRegistry = codecRegistry;
    this.environment = environment;
    store = environment.computeInTransaction(tx -> {
      LOG.info("Opening store {}", storeName);
      return environment.openStore(storeName, storeConfig, tx);
    });
  }

  private static byte[] byteIterableToArray(ByteIterable binaryData) {
    final ByteIterator iterator = binaryData.iterator();
    final ByteArrayOutputStream arrayOutput = new ByteArrayOutputStream();
    while (iterator.hasNext()) {
      arrayOutput.write(iterator.next());
    }
    return arrayOutput.toByteArray();
  }

  @Override
  public void persist(Identifiable identifiable, Object data) {

    final String key = identifiable.getKey();
    final Class<?> type = data.getClass();
    LOG.info("Persisting key[{}]|type[{}]", key, type);

    final DataCodec codec = checkedGetCodec(type);

    environment.executeInTransaction(tx -> {
      final ByteIterable binaryKey = stringToEntry(key);

      if (store.get(tx, binaryKey) != null) {
        LOG.debug("Previous data found for {}, removing", key);
        store.delete(tx, binaryKey);
      }

      final ByteIterable binaryData = new ByteBufferByteIterable(wrap(codec.toBinary(data)));
      LOG.debug("Putting data for {}", key);
      store.put(tx, binaryKey, binaryData);
    });
    LOG.info("Data successfully persisted for key[{}]|type[{}]", key, type);
  }

  @Override
  public <T> Optional<T> read(Identifiable identifiable, Class<T> type) {
    final String key = identifiable.getKey();
    LOG.info("Reading data for key[{}]|type[{}]", key, type);
    final ByteIterable binaryData = environment.computeInReadonlyTransaction(tx ->
        store.get(tx, stringToEntry(key)));

    if (binaryData == null) {
      LOG.warn("No data found for key {}", key);
      return Optional.empty();
    }

    final DataCodec<T> codec = checkedGetCodec(type);

    final T value = codec.fromBinary(byteIterableToArray(binaryData));
    if (value == null) {
      LOG.warn("Unable to deserialize data for key {}", key);
      return Optional.empty();
    }
    return Optional.of(value);
  }

  private <T> DataCodec<T> checkedGetCodec(Class<T> type) {
    final DataCodec codec = codecRegistry.getCodec(type);
    if (codec == null) {
      throw new IllegalStateException(format("No codec found for type %s", type));
    }
    return codec;
  }
}
