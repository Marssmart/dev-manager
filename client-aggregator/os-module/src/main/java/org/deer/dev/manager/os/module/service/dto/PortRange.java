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

package org.deer.dev.manager.os.module.service.dto;

import static com.google.common.base.Preconditions.checkArgument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PortRange {

  private final int rangeStart;
  private final int rangeEnd;

  @JsonCreator
  PortRange(@JsonProperty(value = "rangeStart", required = true) final int rangeStart,
      @JsonProperty(value = "rangeEnd", required = true) final int rangeEnd) {
    checkArgument(rangeEnd != rangeStart, "Empty range[%s - %s]", rangeStart, rangeEnd);
    checkArgument(rangeEnd > rangeStart, "Invalid range [%s - %s]", rangeStart, rangeEnd);
    this.rangeStart = rangeStart;
    this.rangeEnd = rangeEnd;
  }

  public int getRangeStart() {
    return rangeStart;
  }

  public int getRangeEnd() {
    return rangeEnd;
  }

  public int batches(final int batchSize) {
    final int difference = rangeEnd - rangeStart;
    return (difference / batchSize) + (difference % batchSize == 0 ? 0 : 1);
  }
}
