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

package org.deer.dev.manager.persist.xodus;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class XodusPersistenceEnabledCondition implements Condition {

  private static final Logger LOG = LoggerFactory.getLogger(XodusPersistenceEnabledCondition.class);

  @Override
  public boolean matches(ConditionContext conditionContext,
      AnnotatedTypeMetadata annotatedTypeMetadata) {
    final Environment environment = conditionContext.getEnvironment();

    final boolean xodusPersistenceEnabled = Boolean.valueOf(
        environment.getProperty("xodus.persistence.enabled", "false"));
    LOG.trace("Xodus persistence enabled = {}", xodusPersistenceEnabled);
    return xodusPersistenceEnabled;
  }
}
