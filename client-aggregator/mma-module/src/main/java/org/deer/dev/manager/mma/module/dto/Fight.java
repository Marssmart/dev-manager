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

package org.deer.dev.manager.mma.module.dto;

import java.time.Duration;
import java.time.LocalDate;
import org.deer.dev.manager.mma.module.dto.enumerated.FightEnd;

public class Fight {

  public static final String[] CSV_COLUMNS = {"date", "event", "fightEnd", "fightEndType",
      "fighterRef", "opponentRef", "stopageRound", "stopageTime"};

  public static final Integer[] RELEVANT_CSV_COL_INDEXES = new Integer[]{1, 2, 3, 4, 5, 9, 10, 11};

  private LocalDate date;
  private String event;
  private FightEnd fightEnd;
  private String fightEndType;
  private long fighterRef;
  private long opponentRef;
  private int stopageRound;
  private Duration stopageTime;

  public Fight(LocalDate date, String event, FightEnd fightEnd, String fightEndType,
      long fighterRef, long opponentRef, int stopageRound, Duration stopageTime) {
    this.date = date;
    this.event = event;
    this.fightEnd = fightEnd;
    this.fightEndType = fightEndType;
    this.fighterRef = fighterRef;
    this.opponentRef = opponentRef;
    this.stopageRound = stopageRound;
    this.stopageTime = stopageTime;
  }

  public Fight() {
    this(null, null, null, null, 0, 0, 0, null);
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getEvent() {
    return event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public FightEnd getFightEnd() {
    return fightEnd;
  }

  public void setFightEnd(FightEnd fightEnd) {
    this.fightEnd = fightEnd;
  }

  public String getFightEndType() {
    return fightEndType;
  }

  public void setFightEndType(String fightEndType) {
    this.fightEndType = fightEndType;
  }

  public long getFighterRef() {
    return fighterRef;
  }

  public void setFighterRef(long fighterRef) {
    this.fighterRef = fighterRef;
  }

  public long getOpponentRef() {
    return opponentRef;
  }

  public void setOpponentRef(long opponentRef) {
    this.opponentRef = opponentRef;
  }

  public int getStopageRound() {
    return stopageRound;
  }

  public void setStopageRound(int stopageRound) {
    this.stopageRound = stopageRound;
  }

  public Duration getStopageTime() {
    return stopageTime;
  }

  public void setStopageTime(Duration stopageTime) {
    this.stopageTime = stopageTime;
  }
}
