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

package org.deer.dev.manager.mma.module;

import javax.sql.DataSource;
import org.deer.dev.manager.mma.module.cache.FighterCache;
import org.deer.dev.manager.mma.module.dto.Fighter;
import org.deer.dev.manager.mma.module.processor.FighterProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class MmaModuleConfig extends DefaultBatchConfigurer {

  @Value("${mma.fighter.file.path}")
  private String fighterFilePath;

  @Autowired
  public JobBuilderFactory jobBuilderFactory;

  @Autowired
  public StepBuilderFactory stepBuilderFactory;

  @Override
  public void setDataSource(DataSource dataSource) {
    // to ignore DataSource init
  }

  @Bean("fighterReader")
  public FlatFileItemReader<Fighter> fighterReader() {
    return new FlatFileItemReaderBuilder<Fighter>()
        .name("fighterReader")
        .resource(new FileSystemResource(fighterFilePath))
        .linesToSkip(1)//skip header
        .delimited()
        .delimiter(";")
        .includedFields(Fighter.RELEVANT_CSV_COLUMENTS)
        .names(Fighter.CSV_COLUMNS)
        .fieldSetMapper(new BeanWrapperFieldSetMapper<Fighter>() {{
          setTargetType(Fighter.class);
        }})
        .build();
  }

  @Bean("fighterImportJob")
  public Job importUserJob(@Qualifier("fighterImportStep") Step step,
      final FighterCache fighterCache) {
    return jobBuilderFactory.get("importUserJob")
        .listener(fighterCache)
        .flow(step)
        .end()
        .build();
  }

  @Bean("fighterImportStep")
  public Step fighterImportStep(final ItemReader<Fighter> fighterReader,
      final FighterProcessor fighterProcessor,
      final FighterCache fighterCache) {
    return stepBuilderFactory.get("fighterImportStep")
        .<Fighter, Fighter>chunk(100)
        .reader(fighterReader)
        .processor(fighterProcessor)
        .writer(fighterCache)
        .build();
  }
}
