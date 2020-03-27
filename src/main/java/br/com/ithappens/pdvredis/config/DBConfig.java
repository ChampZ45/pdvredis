package br.com.ithappens.pdvredis.config;

import br.com.ithappens.pdvredis.mapper.tesouraria.TesourariaMovimentoMapper;
import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties
@EnableTransactionManagement
@MapperScan("br.com.ithappens.pdvredis.mapper.*")
public class DBConfig {
     @Bean
     @Primary
     @ConfigurationProperties("app.financeiro-datasource")
     public DataSourceProperties mainDataSourceProperties() {
          return new DataSourceProperties();
     }

     @Bean
     @Primary
     @ConfigurationProperties("app.financeiro-datasource.hikari")
     public HikariDataSource mainDatasource(DataSourceProperties properties) {
          return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
     }

     @Bean
     @Primary
     public DataSourceTransactionManager mainDataSourceTransactionManager(DataSource dataSource) {
          return new DataSourceTransactionManager(dataSource);
     }

     @Bean
     @Primary
     public SqlSessionFactoryBean mainSqlSessionFactoryBean(@Qualifier("mainDatasource")
                                                                         DataSource dataSource,
                                                            MybatisProperties properties,
                                                            ResourceLoader resourceLoader) {
          return getSqlSessionFactoryBean(dataSource, properties, resourceLoader);
     }

     //=========================================================================

     @Bean
     @ConfigurationProperties("app.tesouraria-datasource")
     public DataSourceProperties tesourariaDataSourceProperties() {
          return new DataSourceProperties();
     }

     @Bean
     @ConfigurationProperties("app.tesouraria-datasource.hikari")
     public HikariDataSource tesourariaDatasource(@Qualifier("tesourariaDataSourceProperties")
                                                          DataSourceProperties properties) {
          return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
     }

     @Bean
     public DataSourceTransactionManager tesourariaDataSourceTransactionManager(@Qualifier("tesourariaDatasource")
                                                                                        DataSource dataSource) {
          return new DataSourceTransactionManager(dataSource);
     }

     @Bean
     public SqlSessionFactoryBean tesourariaSqlSessionFactoryBean(@Qualifier("tesourariaDatasource")
                                                                               DataSource dataSource,
                                                                  MybatisProperties properties,
                                                                  ResourceLoader resourceLoader) {
          return getSqlSessionFactoryBean(dataSource, properties, resourceLoader);
     }

     //=========================================================================

     @Bean
     @ConfigurationProperties("app.backoffice-datasource")
     public DataSourceProperties backofficeDataSourceProperties() {
          return new DataSourceProperties();
     }

     @Bean
     @ConfigurationProperties("app.backoffice-datasource.hikari")
     public HikariDataSource backofficeDatasource(@Qualifier("backofficeDataSourceProperties")
                                                          DataSourceProperties properties) {
          return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
     }

     @Bean
     public DataSourceTransactionManager backofficeDataSourceTransactionManager(@Qualifier("backofficeDatasource")
                                                                                        DataSource dataSource) {
          return new DataSourceTransactionManager(dataSource);
     }

     @Bean
     public SqlSessionFactoryBean backofficeSqlSessionFactoryBean(@Qualifier("backofficeDatasource")
                                                                               DataSource dataSource,
                                                                  MybatisProperties properties,
                                                                  ResourceLoader resourceLoader) {
          return getSqlSessionFactoryBean(dataSource, properties, resourceLoader);
     }

     //=========================================================================
     @Bean
     public MapperFactoryBean<TesourariaMovimentoMapper> tesourariaMovimentoMapper(
             @Qualifier("tesourariaSqlSessionFactoryBean")
                     SqlSessionFactoryBean sqlSessionFactoryBean) throws Exception {

          MapperFactoryBean<TesourariaMovimentoMapper> factoryBean =
                  new MapperFactoryBean<>(TesourariaMovimentoMapper.class);
          factoryBean.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
          return factoryBean;
     }

     private SqlSessionFactoryBean getSqlSessionFactoryBean(@Qualifier("mainDatasource") DataSource dataSource,
                                                            MybatisProperties properties, ResourceLoader resourceLoader) {
          final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
          sqlSessionFactoryBean.setDataSource(dataSource);
          sqlSessionFactoryBean.setVfs(SpringBootVFS.class);

          if (StringUtils.hasText(properties.getConfigLocation())) {
               sqlSessionFactoryBean.setConfigLocation(resourceLoader.getResource(properties.getConfigLocation()));
          }
          org.apache.ibatis.session.Configuration configuration = properties.getConfiguration();
          if (configuration == null && !StringUtils.hasText(properties.getConfigLocation())) {
               configuration = new org.apache.ibatis.session.Configuration();
          }
          sqlSessionFactoryBean.setConfiguration(configuration);
          if (properties.getConfigurationProperties() != null) {
               sqlSessionFactoryBean.setConfigurationProperties(properties.getConfigurationProperties());
          }
          if (StringUtils.hasLength(properties.getTypeAliasesPackage())) {
               sqlSessionFactoryBean.setTypeAliasesPackage(properties.getTypeAliasesPackage());
          }
          if (StringUtils.hasLength(properties.getTypeHandlersPackage())) {
               sqlSessionFactoryBean.setTypeHandlersPackage(properties.getTypeHandlersPackage());
          }
          if (!ObjectUtils.isEmpty(properties.resolveMapperLocations())) {
               sqlSessionFactoryBean.setMapperLocations(properties.resolveMapperLocations());
          }

          return sqlSessionFactoryBean;
     }
}