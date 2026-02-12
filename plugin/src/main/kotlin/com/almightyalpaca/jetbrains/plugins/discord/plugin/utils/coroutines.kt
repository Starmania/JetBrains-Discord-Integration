/*
 * Copyright 2017-2020 Aljoscha Grebe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.almightyalpaca.jetbrains.plugins.discord.plugin.utils

import java.util.concurrent.CompletableFuture
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Converts a [Deferred] to a [CompletableFuture] without relying on
 * `kotlinx.coroutines.future.FutureKt.asCompletableFuture`, which can cause classloader constraint
 * violations when the coroutines library is loaded by both the plugin classloader and the platform
 * classloader.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Deferred<T>.toCompletableFuture(): CompletableFuture<T> {
    val future = CompletableFuture<T>()
    invokeOnCompletion { throwable ->
        if (throwable != null) {
            future.completeExceptionally(throwable)
        } else {
            future.complete(getCompleted())
        }
    }
    return future
}
