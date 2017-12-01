import java.io.File
import java.nio.file.{Files, Path}
import java.nio.file.Files._
import java.nio.file.Paths._
import java.util.concurrent.TimeUnit._

import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration

object CleanDuplicatedFiles {

  def main(args: Array[String]): Unit = {

    if (args.length == 3) {
      val reference = new File(args(0))
      val mess = new File(args(1))
      val action = args(2)

      if (reference.exists && reference.isDirectory && mess.exists && mess.isDirectory) {

        val start = System.currentTimeMillis()

        showMessage("Finding duplicates, it can take a while...")

        findAll(reference.toPath, mess.toPath)
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

        val elapsedTime = Duration(System.currentTimeMillis() - start, MILLISECONDS)
        showMessage(s"Elapsed time: ${if (elapsedTime.toSeconds <= 60) s"${elapsedTime.toSeconds} s" else s"${elapsedTime.toMinutes} min"}")

      } else wrongUsage
    } else wrongUsage
  }

  private def wrongUsage = {
    showMessage("2 real folders are required! Usage: sbt \"run <reference folder> <mess folder> <action: dry-run OR delete-duplicates>\"")
    sys.exit()
  }

  private def showMessage(content: String): Unit = println(s"\n---\n$content\n---\n")

  def newProgress() {
    processedFiles = processedFiles + 1
    val percent = ((processedFiles / messFiles.toDouble) * 100).toInt
    print(s"""\r[${"▇" * percent}${"-" * (100 - (percent + 1))}]""")
  }


  //TODO maybe returning is a list can be a memory eater
  private val duplicates = ListBuffer[String]()
  var messFiles = 0
  var processedFiles = 0

  def findAll(withReference: Path, inMess: Path): ListBuffer[String] = {
    Files.walk(inMess).filter(isRegularFile(_)).forEach(_ => messFiles = messFiles + 1)
    findDuplicates(withReference, inMess)
    duplicates
  }

  private def findDuplicates(referenceFolder: Path, messFolder: Path): Unit = {
    val files = Files.walk(messFolder)
    try {
      files.filter(isRegularFile(_)).forEach { messFile =>
        newProgress()
        if (containsADuplicate(messFile, referenceFolder)) {
          duplicates += messFile.toFile.getAbsolutePath
        }
      }
    } finally {
      files.close()
    }

  }

  private def containsADuplicate(messFile: Path, allReferenceFiles: Path): Boolean = {
    val files = Files.walk(allReferenceFiles)
    try {
      files.filter(isRegularFile(_)).forEach { refFile =>
        if (filesAreDuplicates(messFile, refFile)) return true
      }
      false
    } finally {
      files.close()
    }
  }

  private def filesAreDuplicates(messFile: Path, referenceFile: Path): Boolean =
    (messFile.getFileName == referenceFile.getFileName) && (size(messFile) == size(referenceFile))

}
