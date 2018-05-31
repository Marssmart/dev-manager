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

package org.deer.dev.manager.mma.module.cache;

import static java.lang.String.format;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.PostConstruct;
import org.deer.dev.manager.mma.module.dto.Fighter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Repository;

/**
 * In memory cache of Fighters
 */
@Repository
public class FighterCache extends JobExecutionListenerSupport implements ItemWriter<Fighter> {

  private static final Logger LOG = LoggerFactory.getLogger(FighterCache.class);

  private final List<Fighter> fighters = new LinkedList<>();

  private final Lock initLock = new ReentrantLock();

  @PostConstruct
  public void init() {
    initLock.lock();
  }

  @Override
  public void afterJob(JobExecution jobExecution) {
    if (jobExecution.isRunning()) {
      return;
    }

    // if successfully completed, unlock cache
    if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
      LOG.info("Fighter cache initialization completed");
      initLock.unlock();
    } else {
      throw new IllegalStateException(
          format("Unable to initialize Fighter cache - %s", jobExecution.getExitStatus()));
    }
  }

  @Override
  public void write(List<? extends Fighter> items) throws Exception {
    fighters.addAll(items);
  }

}
