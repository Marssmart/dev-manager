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

package org.deer.dev.manager.os.module.service.service;


import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * SystemTray used for notifications can't be retrieved on linux based os
 */
public class LinuxEnvironmentRule implements TestRule {

  @Override
  public Statement apply(Statement statement, Description description) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        if (IS_OS_LINUX) {
          throw new AssumptionViolatedException("Linux OS, skipping test");
        }
        statement.evaluate();
      }
    };
  }
}
