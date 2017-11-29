import java.io.File

class FindDuplicatesTest extends org.specs2.mutable.Specification {

  "In test folder, we should find duplicates" >> {

    val referenceFolder = new File("src/test/pictures_root/reference")
    val messFolder = new File("src/test/pictures_root/mess")

    CleanDuplicatedFiles.findAll(referenceFolder, messFolder) must
      equalTo(List(
        s"${messFolder.getAbsolutePath}/m1/ff2",
        s"${messFolder.getAbsolutePath}/m1/f3",
        s"${messFolder.getAbsolutePath}/f4",
        s"${messFolder.getAbsolutePath}/f1"))
  }
}