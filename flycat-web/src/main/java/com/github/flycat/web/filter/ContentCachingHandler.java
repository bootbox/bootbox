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
package com.github.flycat.web.filter;

import com.github.flycat.web.util.HttpRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

public interface ContentCachingHandler {

    default boolean executeNextFilter(HttpRequestWrapper requestWrapper,
                                      ContentCachingResponseWrapper responseWrapper) {
        return true;
    }

    default String replaceResponse(String originResponse) {
        return originResponse;
    }

    default PostFilterAction postFilter(HttpRequestWrapper requestWrapper,
                                        ContentCachingResponseWrapper responseWrapper) {
        return new PostFilterAction(false, false);
    }
}
