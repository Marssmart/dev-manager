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

package org.deer.dev.manager.client;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.io.Resources;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

public class DevManagerBanner implements Banner {

  @Override
  public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
    final URL bannerResource = Resources.getResource("banner.txt");

    try {
      Resources.readLines(bannerResource, UTF_8).forEach(out::println);
    } catch (IOException e) {
      throw new IllegalStateException(
          format("Unable to read banner resource from URL %s", bannerResource), e);
    }
  }
}
