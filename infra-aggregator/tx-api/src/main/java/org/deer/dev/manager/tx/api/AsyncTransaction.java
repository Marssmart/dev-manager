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

package org.deer.dev.manager.tx.api;

import java.util.concurrent.CompletableFuture;

/**
 * Adds async capabilities to {@link Transaction}
 * */
public interface AsyncTransaction extends Transaction {

  /**
   * Async version of commit() method
   */
  default CompletableFuture<Void> asyncCommit() {
    return CompletableFuture.runAsync(this::commit);
  }

  /**
   * Async version of rollback() method
   */
  default CompletableFuture<Void> asyncRollback() {
    return CompletableFuture.runAsync(this::rollback);
  }
}
