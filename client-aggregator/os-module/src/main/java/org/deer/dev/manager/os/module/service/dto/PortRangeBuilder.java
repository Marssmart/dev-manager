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

public class PortRangeBuilder {

  private int rangeStart;
  private int rangeEnd;

  public PortRangeBuilder setRangeStart(int rangeStart) {
    this.rangeStart = rangeStart;
    return this;
  }

  public PortRangeBuilder setRangeEnd(int rangeEnd) {
    this.rangeEnd = rangeEnd;
    return this;
  }

  public PortRange createPortRange() {
    return new PortRange(rangeStart, rangeEnd);
  }
}