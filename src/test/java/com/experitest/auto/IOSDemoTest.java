package com.experitest.auto;

import org.testng.annotations.*;

public class IOSDemoTest extends BaseTest {
	@BeforeMethod
	@Parameters("deviceQuery")
	public void setUp(@Optional("@os='ios'") String deviceQuery) throws Exception{
		init(deviceQuery, "IOSDemoTest");
	}
	
	@Test
	public void test(){
		
	}
	

}
