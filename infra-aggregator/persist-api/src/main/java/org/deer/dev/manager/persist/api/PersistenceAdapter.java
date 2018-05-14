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

import java.util.Optional;

/**
 * Adapter to allow persisting content of transactions
 */
public interface PersistenceAdapter {

  /**
   * Persists data under the key obtained from provider {@link Identifiable}
   *
   * @param identifiable unique identifier of object
   * @param data object to persist
   */
  void persist(final Identifiable identifiable, final Object data);

  /**
   * Read's data identified by identifier
   *
   * @param identifiable unique identifier of object
   * @param type type of the read data
   * @return Object of the desired type stored under the provided key
   */
  <T> Optional<T> read(final Identifiable identifiable, final Class<T> type);
}
