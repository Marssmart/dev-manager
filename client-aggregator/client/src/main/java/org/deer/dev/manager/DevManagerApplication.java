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

package org.deer.dev.manager;


import org.deer.dev.manager.client.DevManagerBanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})// to exclude DataSource cfg
public class DevManagerApplication {

  private static final Logger LOG = LoggerFactory.getLogger(DevManagerApplication.class);

  /**
   * Starts the whole application
   */
  public static void main(String[] args) throws InterruptedException {
    LOG.info("Starting Dev-Manager application");

    new SpringApplicationBuilder()
        .sources(DevManagerApplication.class)
        .banner(new DevManagerBanner())
        .bannerMode(Mode.LOG)
        .build()
        .run(args);

    LOG.info("Dev-Manager application started");
    Thread.currentThread().join();
  }

}
