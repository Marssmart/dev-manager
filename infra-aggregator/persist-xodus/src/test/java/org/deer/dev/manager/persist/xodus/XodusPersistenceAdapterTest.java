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

import static java.nio.charset.StandardCharsets.UTF_8;
import static jetbrains.exodus.bindings.StringBinding.stringToEntry;
import static jetbrains.exodus.env.StoreConfig.WITHOUT_DUPLICATES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import jetbrains.exodus.ArrayByteIterable;
import jetbrains.exodus.ByteIterable;
import jetbrains.exodus.bindings.StringBinding;
import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.Store;
import org.deer.dev.manager.persist.api.DataCodec;
import org.deer.dev.manager.persist.api.DataCodecRegistry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class XodusPersistenceAdapterTest {

  private static final String TEST_KEY = "testKey";
  private static final String TEST_DATA = "testData";

  @Mock
  private DataCodecRegistry codecRegistry;

  private Environment environment;
  private XodusPersistenceAdapter adapter;
  private Store store;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    environment = Environments.newInstance("/tmp/test");
    adapter = new XodusPersistenceAdapter(codecRegistry, environment,
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
  public void testPersistNoCodec() {
    adapter.persist(() -> TEST_KEY, TEST_DATA);
  }

  @Test
  public void testPersistWithCodec() {
    final DataCodec codec = mock(DataCodec.class);
    when(codec.toBinary(any(String.class)))
        .thenAnswer(invocationOnMock -> String.class.cast(
            invocationOnMock.getArgument(0)).getBytes(UTF_8));
    when(codecRegistry.getCodec(String.class)).thenReturn(codec);

    adapter.persist(() -> TEST_KEY, TEST_DATA);

    final ByteIterable persistedBinaryData = environment
        .computeInTransaction(tx -> store.get(tx, stringToEntry(TEST_KEY)));

    final String persistedData = new String(persistedBinaryData.getBytesUnsafe(), UTF_8);
    assertEquals(TEST_DATA, persistedData);
  }

  @Test
  public void testPersistWithCodecAndPreviousData() {
    environment.executeInTransaction(tx ->
        store.put(tx, stringToEntry(TEST_KEY), stringToEntry(TEST_DATA)));

    final DataCodec codec = mock(DataCodec.class);
    when(codec.toBinary(any(String.class)))
        .thenAnswer(invocationOnMock -> String.class.cast(
            invocationOnMock.getArgument(0)).getBytes(UTF_8));
    when(codecRegistry.getCodec(String.class)).thenReturn(codec);

    adapter.persist(() -> TEST_KEY, TEST_DATA);

    final ByteIterable persistedBinaryData = environment
        .computeInTransaction(tx -> store.get(tx, stringToEntry(TEST_KEY)));

    final String persistedData = new String(persistedBinaryData.getBytesUnsafe(), UTF_8);
    assertEquals(TEST_DATA, persistedData);
  }

  @Test
  public void testReadNoData() {
    assertFalse("No data should be present",
        adapter.read(() -> TEST_KEY, String.class).isPresent());
  }

  @Test(expected = IllegalStateException.class)
  public void testReadNoCodec() {
    environment.executeInTransaction(tx ->
        store.put(tx, stringToEntry(TEST_KEY), stringToEntry(TEST_DATA)));

    final Optional<String> read = adapter.read(() -> TEST_KEY, String.class);
    assertFalse("No data should be present", read.isPresent());
  }

  @Test
  public void testReadBadCodec() {
    environment.executeInTransaction(tx ->
        store.put(tx, stringToEntry(TEST_KEY), stringToEntry(TEST_DATA)));

    final DataCodec noopCodec = mock(DataCodec.class);
    when(codecRegistry.getCodec(String.class)).thenReturn(noopCodec);

    final Optional<String> read = adapter.read(() -> TEST_KEY, String.class);
    assertFalse("Codec should return null", read.isPresent());
  }

  @Test
  public void testRead() {
    environment.executeInTransaction(tx ->
        store.put(tx, stringToEntry(TEST_KEY), stringToEntry(TEST_DATA)));

    final DataCodec codec = mock(DataCodec.class);
    when(codec.fromBinary(any())).thenAnswer(invocationOnMock -> {
      byte[] binaryData = invocationOnMock.getArgument(0);
      //NOTE - for whatever reason StringBinding is adding 0 on the end of the array
      // so using this to compensate for it
      return StringBinding.entryToString(new ArrayByteIterable(binaryData));
    });
    when(codecRegistry.getCodec(String.class)).thenReturn(codec);

    final Optional<String> read = adapter.read(() -> TEST_KEY, String.class);
    assertTrue("Data should be non null", read.isPresent());
    assertEquals(TEST_DATA, read.get());
  }
}