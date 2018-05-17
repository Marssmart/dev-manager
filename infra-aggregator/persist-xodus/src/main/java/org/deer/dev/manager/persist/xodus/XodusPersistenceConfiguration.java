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

import jetbrains.exodus.env.Environment;
import jetbrains.exodus.env.Environments;
import jetbrains.exodus.env.StoreConfig;
import org.deer.dev.manager.persist.api.PersistenceAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(XodusPersistenceEnabledCondition.class)
public class XodusPersistenceConfiguration {

  @Value("${xodus.storage.path}")
  private String xodusStoragePath;

  @Value("${xodus.storage.name}")
  private String xodusStorageName;

  @Value("${xodus.storage.config}")
  private StoreConfig xodusStorageConfig;

  @Bean
  public Environment createXodusEnvironment() {
    return Environments.newInstance(xodusStoragePath);
  }

  @Bean
  public PersistenceAdapter xosusPersistenceAdapter(Environment environment) {
    return new XodusPersistenceAdapter(environment,
        xodusStorageName, xodusStorageConfig);
  }
}
