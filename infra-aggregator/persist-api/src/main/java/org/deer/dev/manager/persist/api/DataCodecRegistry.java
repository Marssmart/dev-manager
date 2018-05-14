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

package org.deer.dev.manager.persist.api;

/**
 * Registry for codecs to convert data to/from binary format
 */
public interface DataCodecRegistry {

  /**
   * Registers codec for desired type
   */
  <T> void register(final Class<T> type, final DataCodec<T> codec);

  /**
   * Finds respective codec by provided type
   */
  <T> DataCodec<T> getCodec(final Class<T> type);

  /**
   * Finds codec by ordinal
   */
  DataCodec<?> getCodec(final int ordinal);
}
