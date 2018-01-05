package com.experitest.auto;

import org.testng.annotations.*;

public class AndroidDemoTest extends BaseTest
{

	@BeforeMethod
	@Parameters("deviceQuery")
	public void setUp(@Optional("@os='android'") String deviceQuery) throws Exception
	{
		init(deviceQuery, "AndroidDemoTest");
	}

	@Test
	public void test()
	{

	}

}
