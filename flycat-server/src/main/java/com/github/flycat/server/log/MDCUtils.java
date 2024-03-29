/**
 * Copyright 2019 zgqq <zgqjava@gmail.com>
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
 */
package com.github.flycat.server.log;

import com.google.common.collect.Sets;

import java.util.Set;

public final class MDCUtils {


    public static final String LOG_MDC = "logMDC";

    public static final String HTTP_URI = "req.requestURI";
    public static final String HTTP_METHOD = "req.method";
    public static final String HTTP_AGENT = "req.userAgent";

    public static final Set<String> MDC_CONSTANTS = Sets.newHashSet(HTTP_URI,
            HTTP_METHOD);

}
