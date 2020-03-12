package com.metrics.mapper;

import org.springframework.context.annotation.Configuration;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;

import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;
@Configuration
interface MetricMapping  extends OrikaMapperFactoryConfigurer
{
	 	@Override
		public default void configure(MapperFactory orikaMapperFactory) {
			orikaMapperFactory.classMap(CreateMetricRequest.class, MetricsCollection.class).byDefault().register();
		}

}
