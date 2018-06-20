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
import org.deer.dev.manager.common.Validations;
import org.deer.dev.manager.mma.module.dto.Fighter;
import org.deer.dev.manager.mma.module.dto.builder.FighterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FighterProcessor implements ItemProcessor<Fighter, Fighter>, Validations {

  private static final Logger LOG = LoggerFactory.getLogger(FighterProcessor.class);

  @Override
  public Fighter process(Fighter item) throws Exception {
    LOG.trace("Processing fighter {}", item);

    if (!new EqualsBuilder()
        .append(true, isNotNullAndNonEmptyTrimmed(item.getProfileLink()))
        .append(true, item.getRef() != 0)
        .isEquals()) {
      LOG.error("Fighter {} filtered out as invalid", item);
      return null;// it will make it filter out
      //https://docs.spring.io/spring-batch/trunk/reference/html/readersAndWriters.html- 6.2.3
    }

    return new FighterBuilder()
        .setFirstName(item.getFirstName().trim())
        .setLastName(item.getLastName().trim())
        .setProfileLink(item.getProfileLink().trim())
        .setRef(item.getRef())
        .createFighter();
  }
}
