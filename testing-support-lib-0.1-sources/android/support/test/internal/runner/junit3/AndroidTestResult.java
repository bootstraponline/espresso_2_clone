/*
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.test.internal.runner.junit3;

import android.app.Instrumentation;
import android.os.Bundle;
import android.test.AndroidTestCase;
import android.test.InstrumentationTestCase;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.Test;

/**
 * A specialized {@link TestResult} that injects Android constructs into the test if necessary.
 */
class AndroidTestResult extends DelegatingTestResult {

    private final Instrumentation mInstr;
    private final Bundle mBundle;

    AndroidTestResult(Bundle bundle, Instrumentation instr, TestResult result) {
        super(result);
        mBundle = bundle;
        mInstr = instr;
    }

    @Override
    protected void run(final TestCase test) {
        if (test instanceof AndroidTestCase) {
            ((AndroidTestCase)test).setContext(mInstr.getTargetContext());
        }
        if (test instanceof InstrumentationTestCase) {
            ((InstrumentationTestCase)test).injectInstrumentation(mInstr);
        }
        super.run(test);
    }

    /**
     * Notify of test failure and finish execution to successfully report back to
     * instrumentation results
     */
    void notifyFailureAndFinish(Test test, Throwable throwable) {
        addError(test, throwable);
        endTest(test);
    }
}
