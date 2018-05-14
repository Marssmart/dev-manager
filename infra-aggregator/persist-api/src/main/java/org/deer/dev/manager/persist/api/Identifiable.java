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
 * Unique identifier that can identify specific data. Used to persist data
 */
public interface Identifiable {

  /**
   * Bound to using string keys, for now. Gives more leeway while debug. Its easier to work with
   * "key-one" than 154724
   */
  String getKey();
}
