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

package org.deer.dev.manager.mma.module.processor;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.deer.dev.manager.mma.module.dto.Fight;
import org.deer.dev.manager.mma.module.dto.builder.FightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class FightProcessor implements ItemProcessor<Fight, Fight> {

  private static final Logger LOG = LoggerFactory.getLogger(FightProcessor.class);

  @Override
  public Fight process(Fight item) throws Exception {

    LOG.trace("Processing fight {}", item);

    if (!new EqualsBuilder()
        .append(true, item.getFighterRef() != 0)
        .append(true, item.getOpponentRef() != 0)
        .append(true, item.getFightEnd() != null)
        .isEquals()) {
      LOG.error("Fight {} filtered out as invalid", item);
      return null;// it will make it filter out
    }

    return new FightBuilder()
        .setDate(item.getDate())
        .setEvent(item.getEvent())
        .setFightEnd(item.getFightEnd())
        .setFighterRef(item.getFighterRef())
        .setFightEndType(item.getFightEndType())
        .setOpponentRef(item.getOpponentRef())
        .setStopageRound(item.getStopageRound())
        .setStopageTime(item.getStopageTime())
        .createFight();
  }
}
