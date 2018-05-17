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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.util.Collection;
import org.deer.dev.manager.os.module.service.OsInfoService;
import org.deer.dev.manager.os.module.service.dto.SystemProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/os")
public class OsInfoRest {

  @Autowired
  private OsInfoService osInfoService;

  @RequestMapping(path = "/properties/{key}", method = GET, produces = "application/json")
  @ResponseBody
  public SystemProperty getSystemProperty(@PathVariable("key") String propertyKey) {
    return osInfoService.getSystemProperty(propertyKey);
  }

  @RequestMapping(path = "/properties", method = GET, produces = "application/json")
  @ResponseBody
  public Collection<SystemProperty> getSystemProperties() {
    return osInfoService.getAllSystemProperties();
  }

  @RequestMapping(path = "/properties", method = PUT, consumes = "application/json")
  public void setSystemProperty(@RequestBody SystemProperty systemProperty) {
    osInfoService.setSystemProperty(systemProperty);
  }
}
