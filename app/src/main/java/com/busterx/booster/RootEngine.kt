package com.busterx.booster

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

data class RootResult(
    val success: Boolean,
    val output: String = "",
    val error: String = "",
    val exitCode: Int = -1
)

object RootEngine {

    suspend fun check(): RootResult =
        run("id")

    suspend fun setScreenTimeout(seconds: Int): RootResult {
        require(seconds in 30..36000)

        return run(
            "settings",
            "put",
            "system",
            "screen_off_timeout",
            (seconds * 1000).toString()
        )
    }

    suspend fun forceStopPackage(packageName: String): RootResult {
        require(packageName.matches(Regex("[A-Za-z0-9_.]+")))

        val protectedPackages = setOf(
            "android",
            "com.android.systemui",
            "com.busterx.booster"
        )

        if (packageName in protectedPackages) {
            return RootResult(
                success = false,
                error = "Protected system package"
            )
        }

        return run("am", "force-stop", packageName)
    }

    private suspend fun run(vararg args: String): RootResult =
        withContext(Dispatchers.IO) {
            try {
                val command = arrayOf(
                    "su",
                    "-c",
                    args.joinToString(" ") { quote(it) }
                )

                val process = ProcessBuilder(*command)
                    .redirectErrorStream(false)
                    .start()

                val stdout =
                    BufferedReader(
                        InputStreamReader(process.inputStream)
                    ).readText()

                val stderr =
                    BufferedReader(
                        InputStreamReader(process.errorStream)
                    ).readText()

                val exitCode = process.waitFor()

                RootResult(
                    success = exitCode == 0,
                    output = stdout.trim(),
                    error = stderr.trim(),
                    exitCode = exitCode
                )
            } catch (e: Exception) {
                RootResult(
                    success = false,
                    error = e.message ?: "Root execution failed"
                )
            }
        }

    private fun quote(value: String): String =
        "'" + value.replace("'", "'\\''") + "'"
}
