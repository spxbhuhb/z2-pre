/*
 * Copyright (C) 2020 Brian Norman
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

package hu.simplexion.z2.service.gradle

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

@Suppress("unused")
class ServiceGradlePlugin : KotlinCompilerPluginSupportPlugin {

    override fun apply(target: Project): Unit = with(target) {
        extensions.create(GRADLE_EXTENSION_NAME, ServiceGradleExtension::class.java)
    }

    override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean =
        kotlinCompilation.target.project.plugins.hasPlugin(ServiceGradlePlugin::class.java)

    override fun getCompilerPluginId(): String = KOTLIN_COMPILER_PLUGIN_ID

    override fun getPluginArtifact(): SubpluginArtifact = SubpluginArtifact(
        groupId = KOTLIN_COMPILER_PLUGIN_ARTIFACT_GROUP,
        artifactId = KOTLIN_COMPILER_PLUGIN_ARTIFACT_NAME,
        version = PLUGIN_VERSION
    )

    override fun getPluginArtifactForNative(): SubpluginArtifact = SubpluginArtifact(
        groupId = KOTLIN_COMPILER_PLUGIN_ARTIFACT_GROUP,
        artifactId = "$KOTLIN_COMPILER_PLUGIN_ARTIFACT_NAME-native",
        version = PLUGIN_VERSION
    )

    override fun applyToCompilation(
        kotlinCompilation: KotlinCompilation<*>
    ): Provider<List<SubpluginOption>> {
        val project = kotlinCompilation.target.project

        // This does not work for multiplatform projects. So I'll go with manual dependency add for now.
        // TODO check automatic dependency addition from gradle plugin
//        kotlinCompilation.dependencies {
//            implementation("$PLUGIN_GROUP:$RUNTIME_NAME:$PLUGIN_VERSION")
//        }

        val extension = project.extensions.getByType(ServiceGradleExtension::class.java)

        val options = mutableListOf<SubpluginOption>()

        return project.provider { options }
    }
}
