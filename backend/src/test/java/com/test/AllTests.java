package com.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import com.test.service.UserServiceTest;
import com.test.service.ProductServiceTest;
import com.test.service.CartServiceTest;
import com.test.service.OrderServiceTest;

@Suite
@SelectClasses({
    UserServiceTest.class,
    ProductServiceTest.class,
    CartServiceTest.class,
    OrderServiceTest.class
})
public class AllTests {
    // 测试套件入口
}
