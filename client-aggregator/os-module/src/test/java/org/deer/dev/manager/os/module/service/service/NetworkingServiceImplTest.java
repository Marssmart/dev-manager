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

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import org.deer.dev.manager.os.module.service.dto.PortRangeBuilder;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkingServiceImplTest {

  private static final Logger LOG = LoggerFactory.getLogger(NetworkingServiceImplTest.class);

  private NetworkingServiceImpl service;

  @Before
  public void setUp() throws Exception {
    service = new NetworkingServiceImpl();
  }

  @Ignore
  @Test
  public void scanOpenPorts() throws Exception {
    final CompletableFuture<Set<Integer>> scanFuture = service
        .scanOpenTcpPorts(new PortRangeBuilder()
            .setRangeStart(0)
            .setRangeEnd(1024)
            .createPortRange());

    final Set<Integer> ports = scanFuture.join();
    LOG.info("Available ports {}", ports);
  }

}