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

import static jetbrains.exodus.bindings.StringBinding.stringToEntry;
import static jetbrains.exodus.env.StoreConfig.WITHOUT_DUPLICATES;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import org.deer.dev.manager.persist.xodus.XodusPersistence.AnyData;
import org.deer.dev.manager.persist.xodus.test.XodusPersistenceTest.DataDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class XodusPersistenceAdapterTest implements XodusBinaryDataProcessor {

  private static final String TEST_KEY = "testKey";

  private Environment environment;
  private XodusPersistenceAdapter adapter;
  private Store store;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    environment = Environments.newInstance("/tmp/test");
    adapter = new XodusPersistenceAdapter(environment,
        "TestStore", WITHOUT_DUPLICATES);
    store = environment
        .computeInTransaction(tx -> environment.openStore("TestStore", WITHOUT_DUPLICATES, tx));
  }

  @After
  public void tearDown() {
    environment.clear();
    environment.close();
  }

  @Test(expected = IllegalStateException.class)
  public void testNonProtoBufferDto() {
    adapter.persist(() -> TEST_KEY, "non-proto-dto");
  }

  @Test
  public void testPersistEmpty() throws IOException {
    final DataDto data = DataDto.newBuilder().build();
    adapter.persist(() -> TEST_KEY, data);
    final ByteIterable binaryData = environment.computeInReadonlyTransaction(
        tx -> store.get(tx, stringToEntry(TEST_KEY)));

    final AnyData dataWraper = AnyData.parseFrom(byteIterableToStream(binaryData));
    final DataDto deserializedData = dataWraper.getData().unpack(DataDto.class);
    assertEquals(data, deserializedData);
  }
}