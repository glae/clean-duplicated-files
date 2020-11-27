import java.io.File
import org.specs2.mutable.Specification

class CleanDuplicatedFilesTest extends Specification {

  val testReferenceFolder = new File("src/test/pictures_root/reference")
  val testMessFolder = new File("src/test/pictures_root/mess")

  "In test folder, we should find duplicates" >> {

  CleanDuplicatedFiles.findAll(testReferenceFolder.toPath, testMessFolder.toPath).toSet must equalTo(Set(
      s"${testMessFolder.getAbsolutePath}/m1/ff2",
      s"${testMessFolder.getAbsolutePath}/m1/f3",
      s"${testMessFolder.getAbsolutePath}/f1"))
  }

}
