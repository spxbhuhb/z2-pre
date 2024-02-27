

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
        @TestMetadata("defaults.kt")
        public void testDefaults() throws Exception {
            runTest("testData/box/schematic/defaults.kt");
        }

        @Test
        @TestMetadata("fqName.kt")
        public void testFqName() throws Exception {
            runTest("testData/box/schematic/fqName.kt");
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
        @TestMetadata("types.kt")
        public void testTypes() throws Exception {
            runTest("testData/box/services/types.kt");
        }
    }
}
