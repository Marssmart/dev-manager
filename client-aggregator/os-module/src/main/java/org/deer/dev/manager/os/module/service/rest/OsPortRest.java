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

package org.deer.dev.manager.os.module.service.rest;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Set;
import org.deer.dev.manager.os.module.service.dto.PortRange;
import org.deer.dev.manager.os.module.service.service.NetworkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/os/ports")
public class OsPortRest {

  @Autowired
  private NetworkingService networkingService;

  @RequestMapping(path = "/scan-available", method = POST , consumes = "application/json")
  @ResponseBody
  public Set<Integer> scanOpenPorts(@RequestBody final PortRange portRange) {
    return networkingService.scanOpenTcpPorts(portRange).join();
  }
}
