/*
 *
 * (C) Copyright 2018 Ján Srniček (https://github.com/Marssmart)
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

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static java.lang.String.format;
import static java.nio.ByteBuffer.wrap;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;

import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import jetbrains.exodus.ByteBufferByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Store;
import jetbrains.exodus.env.StoreConfig;
import org.deer.dev.manager.persist.api.Identifiable;
import org.deer.dev.manager.persist.api.PersistenceAdapter;
import org.deer.dev.manager.persist.xodus.XodusPersistence.AnyData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


class XodusPersistenceAdapter implements PersistenceAdapter, XodusBinaryDataProcessor {

  private static final Logger LOG = LoggerFactory.getLogger(XodusPersistenceAdapter.class);

  private final Environment environment;
  private final Store store;

  XodusPersistenceAdapter(final Environment environment,
      final String storeName,
      final StoreConfig storeConfig) {
    this.environment = environment;
    store = environment.computeInTransaction(tx -> {
      LOG.info("Opening store {}", storeName);
      return environment.openStore(storeName, storeConfig, tx);
    });
  }

  @Override
  public void persist(Identifiable identifiable, Object data) {
    checkNotNull(data, "Data is null");
    checkState(data instanceof Message, "No support for other that proto-buffers DTO's");
    final String key = identifiable.getKey();
    final Class<?> type = data.getClass();
    LOG.info("Persisting key[{}]|type[{}]", key, type);

    environment.executeInTransaction(tx -> {
      final ByteIterable binaryKey = stringToEntry(key);

      if (store.get(tx, binaryKey) != null) {
        LOG.debug("Previous data found for {}, removing", key);
        store.delete(tx, binaryKey);
      }

      final ByteArrayOutputStream out = wrapAndSerialize(data);

      final ByteIterable binaryData = new ByteBufferByteIterable(wrap(out.toByteArray()));
      LOG.debug("Putting data for {}", key);
      store.put(tx, binaryKey, binaryData);
    });
    LOG.info("Data successfully persisted for key[{}]|type[{}]", key, type);
  }

  @Override
  public <T> Optional<T> read(Identifiable identifiable, Class<T> type) {
    final String key = identifiable.getKey();
    LOG.info("Reading data for key[{}]|type[{}]", key, type);
    final ByteIterable binaryData = environment.computeInReadonlyTransaction(
        tx -> store.get(tx, stringToEntry(key)));

    if (binaryData == null) {
      LOG.warn("No data found for key {}", key);
      return Optional.empty();
    }

    final AnyData parsedData = deserializeWrapped(binaryData);

    //TODO add caching
    final String typeName = parsedData.getType();
    final Class<? extends Message> dtoType = loadDataTypeClass(typeName);

    final Message unpackedData = Optional.ofNullable(parsedData.getData())
        .map(data -> {
          try {
            return data.unpack(dtoType);
          } catch (InvalidProtocolBufferException e) {
            throw new IllegalStateException("Unable to unpack data", e);
          }
        })
        .orElseThrow(
            () -> new IllegalStateException(format("Null data detected for key %s", key)));

    return Optional.of((T) unpackedData);
  }

  private AnyData deserializeWrapped(ByteIterable binaryData) {
    final AnyData parsedData;
    try {
      parsedData = AnyData.parseFrom(byteIterableToArray(binaryData));
    } catch (InvalidProtocolBufferException e) {
      throw new IllegalStateException("Unable to parse data", e);
    }
    return parsedData;
  }

  private static ByteArrayOutputStream wrapAndSerialize(Object data) {
    final AnyData wrappedData = AnyData.newBuilder()
        .setData(Any.pack(Message.class.cast(data)))
        .setType(data.getClass().getName())
        .build();

    final ByteArrayOutputStream out = new ByteArrayOutputStream();

    try {
      wrappedData.writeTo(out);
    } catch (IOException e) {
      throw new IllegalStateException("Unable to write data", e);
    }
    return out;
  }

  private Class<? extends Message> loadDataTypeClass(String typeName) {
    final Class<?> dtoType;
    try {
      dtoType = this.getClass().getClassLoader().loadClass(typeName);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(format("Unable to load type %s", typeName), e);
    }
    checkState(Message.class.isAssignableFrom(dtoType), "%s is not proto-buffer dto class",
        dtoType);
    return dtoType.asSubclass(Message.class);
  }
}
