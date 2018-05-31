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

package org.deer.dev.manager.mma.module.dto.builder;

import java.time.Duration;
import java.time.LocalDate;
import org.deer.dev.manager.mma.module.dto.Fight;
import org.deer.dev.manager.mma.module.dto.enumerated.FightEnd;

public class FightBuilder {

  private LocalDate date;
  private String event;
  private FightEnd fightEnd;
  private String fightEndType;
  private long fighterRef;
  private long opponentRef;
  private int stopageRound;
  private Duration stopageTime;

  public FightBuilder setDate(LocalDate date) {
    this.date = date;
    return this;
  }

  public FightBuilder setEvent(String event) {
    this.event = event;
    return this;
  }

  public FightBuilder setFightEnd(FightEnd fightEnd) {
    this.fightEnd = fightEnd;
    return this;
  }

  public FightBuilder setFightEndType(String fightEndType) {
    this.fightEndType = fightEndType;
    return this;
  }

  public FightBuilder setFighterRef(long fighterRef) {
    this.fighterRef = fighterRef;
    return this;
  }

  public FightBuilder setOpponentRef(long opponentRef) {
    this.opponentRef = opponentRef;
    return this;
  }

  public FightBuilder setStopageRound(int stopageRound) {
    this.stopageRound = stopageRound;
    return this;
  }

  public FightBuilder setStopageTime(Duration stopageTime) {
    this.stopageTime = stopageTime;
    return this;
  }

  public Fight createFight() {
    return new Fight(date, event, fightEnd, fightEndType, fighterRef, opponentRef, stopageRound,
        stopageTime);
  }
}