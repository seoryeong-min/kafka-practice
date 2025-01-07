/*
 * Copyright 2025 Seoryeong Min.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package com.kafkapractice.serializer;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerGenerator {

    private static final AtomicInteger counter = new AtomicInteger(0);
    private static final Random rand = new Random();
    private static final String[] NAMES = {"Adam Bell", "Anthony Claire", "Colter Stevens",
        "David Loki", "Donald Darko", "Jack Twist", "Louis Bloom", "Morf Vandewalt",
        "Quentin Beck"};

    public static Customer getNext() {
        return new Customer(counter.incrementAndGet(), NAMES[rand.nextInt(NAMES.length)]);
    }
}
