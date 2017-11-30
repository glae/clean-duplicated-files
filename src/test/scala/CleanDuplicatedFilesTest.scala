import java.io.File
import org.apache.commons.io.FileUtils

class CleanDuplicatedFilesTest extends org.specs2.mutable.Specification {

  val testReferenceFolder = new File("src/test/pictures_root/reference")
  val testMessFolder = new File("src/test/pictures_root/mess")

  "In test folder, we should find duplicates" >> {

    CleanDuplicatedFiles.findAll(testReferenceFolder.toPath, testMessFolder.toPath).toList must equalTo(List(
      s"${testMessFolder.getAbsolutePath}/m1/ff2",
      s"${testMessFolder.getAbsolutePath}/m1/f3",
      s"${testMessFolder.getAbsolutePath}/f1"))
  }


  "create files" >> {
    create_plenty_of_files()
    true must equalTo(true)
  }

  def create_plenty_of_files() {

    val root = "/tmp/pictures_root2"
    FileUtils.deleteDirectory(new File(root))
    val refName = s"$root/ref"
    val messName = s"$root/mess"
    val ref = new File(refName)
    val mess = new File(messName)
    ref.mkdirs()
    mess.mkdirs()

    for (i <- 1 to 60000) { //files count = 12 * max range
      FileUtils.copyDirectoryToDirectory(testReferenceFolder, ref)
      new File(s"$refName/reference").renameTo(new File(s"$refName/reference$i"))
      FileUtils.copyDirectoryToDirectory(testMessFolder, mess)
      new File(s"$messName/mess").renameTo(new File(s"$messName/mess$i"))
    }

    println("created many files.")
  }
}
