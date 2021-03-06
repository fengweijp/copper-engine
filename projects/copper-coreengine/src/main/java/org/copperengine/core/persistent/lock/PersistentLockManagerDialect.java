/*
 * Copyright 2002-2015 SCOOP Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.copperengine.core.persistent.lock;

import java.sql.Connection;
import java.util.Date;

public interface PersistentLockManagerDialect {

    public String acquireLock(String lockId, String workflowInstanceId, String correlationId, Date insertTS, Connection con) throws Exception;

    public String releaseLock(String lockId, String workflowInstanceId, Connection con) throws Exception;

    public boolean supportsMultipleInstances();

}
