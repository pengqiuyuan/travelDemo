package com.travel;

import java.io.IOException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springside.modules.test.spring.SpringTransactionalTestCase;
import com.sun.el.parser.ParseException;


@ContextConfiguration(locations = {"/applicationContext.xml"})
public class TeTest extends SpringTransactionalTestCase{
	
	@Value("#{envProps.server_url}")
	private String excelUrl;
	
	private static final Logger log = LoggerFactory.getLogger(TeTest.class);
	
	@Test
	public void test() throws IOException, ParseException {	
		log.debug("测试");
	}
}
