

package hu.simplexion.z2.kotlin.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link hu.simplexion.z2.kotlin.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("testData/box")
@TestDataPath("$PROJECT_ROOT")
public class BoxTestGenerated extends AbstractBoxTest {
    @Test
    public void testAllFilesPresentInBox() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
    }

    @Nested
    @TestMetadata("testData/box/adaptive")
    @TestDataPath("$PROJECT_ROOT")
    public class Adaptive {
        @Test
        public void testAllFilesPresentInAdaptive() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adaptive"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("basic.kt")
        public void testBasic() throws Exception {
            runTest("testData/box/adaptive/basic.kt");
        }

        @Test
        @TestMetadata("emptyComponent.kt")
        public void testEmptyComponent() throws Exception {
            runTest("testData/box/adaptive/emptyComponent.kt");
        }

        @Test
        @TestMetadata("emptyEntry.kt")
        public void testEmptyEntry() throws Exception {
            runTest("testData/box/adaptive/emptyEntry.kt");
        }

        @Test
        @TestMetadata("sequence.kt")
        public void testSequence() throws Exception {
            runTest("testData/box/adaptive/sequence.kt");
        }

        @Nested
        @TestMetadata("testData/box/adaptive/select")
        @TestDataPath("$PROJECT_ROOT")
        public class Select {
            @Test
            public void testAllFilesPresentInSelect() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adaptive/select"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
            }

            @Test
            @TestMetadata("ifElse.kt")
            public void testIfElse() throws Exception {
                runTest("testData/box/adaptive/select/ifElse.kt");
            }

            @Test
            @TestMetadata("ifElsePatch.kt")
            public void testIfElsePatch() throws Exception {
                runTest("testData/box/adaptive/select/ifElsePatch.kt");
            }

            @Test
            @TestMetadata("ifOnly.kt")
            public void testIfOnly() throws Exception {
                runTest("testData/box/adaptive/select/ifOnly.kt");
            }
        }

        @Nested
        @TestMetadata("testData/box/adaptive/variables")
        @TestDataPath("$PROJECT_ROOT")
        public class Variables {
            @Test
            public void testAllFilesPresentInVariables() throws Exception {
                KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/adaptive/variables"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
            }

            @Test
            @TestMetadata("basic.kt")
            public void testBasic() throws Exception {
                runTest("testData/box/adaptive/variables/basic.kt");
            }

            @Test
            @TestMetadata("many.kt")
            public void testMany() throws Exception {
                runTest("testData/box/adaptive/variables/many.kt");
            }

            @Test
            @TestMetadata("onlyExternal.kt")
            public void testOnlyExternal() throws Exception {
                runTest("testData/box/adaptive/variables/onlyExternal.kt");
            }

            @Test
            @TestMetadata("onlyInternal.kt")
            public void testOnlyInternal() throws Exception {
                runTest("testData/box/adaptive/variables/onlyInternal.kt");
            }

            @Test
            @TestMetadata("variables.kt")
            public void testVariables() throws Exception {
                runTest("testData/box/adaptive/variables/variables.kt");
            }
        }
    }

    @Nested
    @TestMetadata("testData/box/localization")
    @TestDataPath("$PROJECT_ROOT")
    public class Localization {
        @Test
        public void testAllFilesPresentInLocalization() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/localization"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("basic.kt")
        public void testBasic() throws Exception {
            runTest("testData/box/localization/basic.kt");
        }

        @Test
        @TestMetadata("package.kt")
        public void testPackage() throws Exception {
            runTest("testData/box/localization/package.kt");
        }
    }

    @Nested
    @TestMetadata("testData/box/schematic")
    @TestDataPath("$PROJECT_ROOT")
    public class Schematic {
        @Test
        public void testAllFilesPresentInSchematic() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/schematic"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("companion.kt")
        public void testCompanion() throws Exception {
            runTest("testData/box/schematic/companion.kt");
        }

        @Test
        @TestMetadata("defaults.kt")
        public void testDefaults() throws Exception {
            runTest("testData/box/schematic/defaults.kt");
        }

        @Test
        @TestMetadata("entityCompanion.kt")
        public void testEntityCompanion() throws Exception {
            runTest("testData/box/schematic/entityCompanion.kt");
        }

        @Test
        @TestMetadata("entityReference.kt")
        public void testEntityReference() throws Exception {
            runTest("testData/box/schematic/entityReference.kt");
        }

        @Test
        @TestMetadata("entityStore.kt")
        public void testEntityStore() throws Exception {
            runTest("testData/box/schematic/entityStore.kt");
        }

        @Test
        @TestMetadata("fqName.kt")
        public void testFqName() throws Exception {
            runTest("testData/box/schematic/fqName.kt");
        }

        @Test
        @TestMetadata("generic.kt")
        public void testGeneric() throws Exception {
            runTest("testData/box/schematic/generic.kt");
        }

        @Test
        @TestMetadata("getAndSet.kt")
        public void testGetAndSet() throws Exception {
            runTest("testData/box/schematic/getAndSet.kt");
        }

        @Test
        @TestMetadata("infix.kt")
        public void testInfix() throws Exception {
            runTest("testData/box/schematic/infix.kt");
        }

        @Test
        @TestMetadata("list.kt")
        public void testList() throws Exception {
            runTest("testData/box/schematic/list.kt");
        }

        @Test
        @TestMetadata("listGeneric.kt")
        public void testListGeneric() throws Exception {
            runTest("testData/box/schematic/listGeneric.kt");
        }

        @Test
        @TestMetadata("newInstance.kt")
        public void testNewInstance() throws Exception {
            runTest("testData/box/schematic/newInstance.kt");
        }

        @Test
        @TestMetadata("proto.kt")
        public void testProto() throws Exception {
            runTest("testData/box/schematic/proto.kt");
        }

        @Test
        @TestMetadata("saf.kt")
        public void testSaf() throws Exception {
            runTest("testData/box/schematic/saf.kt");
        }

        @Test
        @TestMetadata("schematicField.kt")
        public void testSchematicField() throws Exception {
            runTest("testData/box/schematic/schematicField.kt");
        }

        @Test
        @TestMetadata("schematicListField.kt")
        public void testSchematicListField() throws Exception {
            runTest("testData/box/schematic/schematicListField.kt");
        }

        @Test
        @TestMetadata("simple.kt")
        public void testSimple() throws Exception {
            runTest("testData/box/schematic/simple.kt");
        }

        @Test
        @TestMetadata("typeArgument.kt")
        public void testTypeArgument() throws Exception {
            runTest("testData/box/schematic/typeArgument.kt");
        }

        @Test
        @TestMetadata("types.kt")
        public void testTypes() throws Exception {
            runTest("testData/box/schematic/types.kt");
        }
    }

    @Nested
    @TestMetadata("testData/box/services")
    @TestDataPath("$PROJECT_ROOT")
    public class Services {
        @Test
        public void testAllFilesPresentInServices() throws Exception {
            KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/box/services"), Pattern.compile("^(.+)\\.kt$"), null, TargetBackend.JVM_IR, true);
        }

        @Test
        @TestMetadata("basic.kt")
        public void testBasic() throws Exception {
            runTest("testData/box/services/basic.kt");
        }

        @Test
        @TestMetadata("context.kt")
        public void testContext() throws Exception {
            runTest("testData/box/services/context.kt");
        }

        @Test
        @TestMetadata("direct.kt")
        public void testDirect() throws Exception {
            runTest("testData/box/services/direct.kt");
        }

        @Test
        @TestMetadata("serviceName.kt")
        public void testServiceName() throws Exception {
            runTest("testData/box/services/serviceName.kt");
        }

        @Test
        @TestMetadata("serviceTransport.kt")
        public void testServiceTransport() throws Exception {
            runTest("testData/box/services/serviceTransport.kt");
        }

        @Test
        @TestMetadata("types.kt")
        public void testTypes() throws Exception {
            runTest("testData/box/services/types.kt");
        }
    }
}
