package com.metrics.mapper;

import com.metrics.domain.CreateMetricRequest;
import com.metrics.model.MetricsCollection;

import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;

public class MetricMapping implements OrikaMapperFactoryConfigurer {

	@Override
	public void configure(MapperFactory orikaMapperFactory) {
		orikaMapperFactory.classMap(CreateMetricRequest.class, MetricsCollection.class).byDefault().register();
	}

}
