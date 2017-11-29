import java.io.File
import java.nio.file.Files._
import java.nio.file.Paths._

import scala.collection.mutable.ListBuffer

object CleanDuplicatedFiles {

  def main(args: Array[String]): Unit = {

    if (args.length == 3) {
      val reference = new File(args(0))
      val mess = new File(args(1))
      val action = args(2)

      if (reference.exists && reference.isDirectory && mess.exists && mess.isDirectory) {

        showMessage("Finding duplicates, it can take a while...")
        val duplicates = findAll(reference, mess)
        showMessage(s"${duplicates.length} duplicates have been found in '${mess.getAbsolutePath}'")

        action match {
          case "dry-run" =>
            showMessage(s"Duplicated files:\n---\n${duplicates.mkString("\n")}")

          case "delete-duplicates" =>
            showMessage("Deleting duplicates, it can take a while...")
            duplicates.foreach(f => delete(get(f)))
            showMessage(s"${duplicates.length} duplicates have been deleted in '${mess.getAbsolutePath}'")

          case _ =>
            showMessage("Please choose a action (dry-run | delete-duplicates).")
        }

      } else wrongUsage
    } else wrongUsage
  }

  private def wrongUsage = {
    showMessage("2 real folders are required! Usage: sbt \"run <reference folder> <mess folder> <action: dry-run OR delete-duplicates>\"")
    sys.exit()
  }

  private def showMessage(content: String): Unit = println(s"\n---\n$content\n---\n")

  //TODO maybe returning is a list can be a memory eater
  private val duplicates = ListBuffer[File]()

  def findAll(withReference: File, inMess: File): List[String] = {
    findDuplicates(withReference, inMess)
    duplicates.map(_.getAbsolutePath).toList
  }

  private def findDuplicates(referenceFolder: File, messFolder: File): Unit = {
    messFolder.listFiles().foreach { messFile =>
      if (messFile.isFile) {
        aDuplicateHasBeenFound = false
        if (containsADuplicate(messFile, referenceFolder)) {
          duplicates += messFile
        }
      } else findDuplicates(referenceFolder, messFile)
    }
  }

  private var aDuplicateHasBeenFound = false

  private def containsADuplicate(messFile: File, allReferenceFiles: File): Boolean = {

    //TODO it would be better to stop when a duplicate has been found
    allReferenceFiles.listFiles().foreach { referenceFile =>
      if (referenceFile.isFile) {
        if (filesAreDuplicates(messFile, referenceFile)) {
          aDuplicateHasBeenFound = true
        }
      } else containsADuplicate(messFile, referenceFile)
    }
    aDuplicateHasBeenFound
  }

  private def filesAreDuplicates(messFile: File, referenceFile: File): Boolean =
    messFile.getName == referenceFile.getName && size(get(messFile.getAbsolutePath)) == size(get(referenceFile.getAbsolutePath))
}
