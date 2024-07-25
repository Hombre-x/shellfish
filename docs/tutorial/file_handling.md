# File Handling

A scripting library does not only contain reading and writing operations, thats why we also include methods for manipulating files such as creations, deletions, permissions and other nice stuff in the library.

## Create a file

You may want to create a file without inmediatly writing on it. For example, when you want to modify the permissions first or let another process/fiber handle it instead. For such and more reasons, you can create a file or directory using the `createFile` and `createDirectory` functions.

This is how you can create a file in Shellfish:

@:select(api-style)

@:choice(syntax)

```scala
import cats.effect.{IO, IOApp}

object Creating extends IOApp.Simple:

  val filePath = Path("path/to/your/desired/creation/NiceScript.scala")

  def run: IO[Unit] =
    for
      _ <- filePath.createFile
      created <- filePath.exists
      _ <- IO.println(s"File created? $created")
    yield ()

end Creating
```

@:choice(static)

```scala
import cats.effect.{IO, IOApp}

object Creating extends IOApp.Simple:

  val filePath = Path("path/to/your/desired/creation/NiceScript.scala")

  def run: IO[Unit] =
    for
      _ <- createFile(filePath)
      created <- exists(filePath)
      _ <- IO.println(s"File created? $created")
    yield ()

end Creating
```

@:@

Here, we are first creating the file using the `createFile` method and then cheking its existante with the `exists` method.

**Important:** The `createFile` and `createDirectory` methods will only work if the parent directory aready exists, otherwise it will fail so be carefull with that. Because of that, if you want to create the parent directories as well you should use the `createDirectories` method like this:

@:select(api-style)

@:choice(syntax)

```scala
import cats.effect.{IO, IOApp}

object Creating extends IOApp.Simple:

  val emptyDirectories = Path("create/me/first")

  def run: IO[Unit] =
    emptyDirectories.createDirectories >> Path("now_i_can_be_created.fs").createFile

end Creating
```

@:choice(static)

```scala
import cats.effect.{IO, IOApp}

object Creating extends IOApp.Simple:

  val emptyDirectories = Path("create/me/first")

  def run: IO[Unit] =
    createDirectories(emptyDirectories) >> createFile(Path("now_i_can_be_created.fs"))

end Creating
```

@:@

### Exercise

Imagine that you need to create a lot nested files on a directory that also needs the creation of the subsecuent folders. For that reason, you need to implement a function that creates both the file and the parent directories. Can you implement it? How?

@:select(api-style)

@:choice(syntax)

```scala
extension (path: Path) def createFileAndDirectories: IO[Unit] = ???
```

@:choice(static)

```scala
def createFileAndDirectories(path: Path): IO[Unit] = ???
```

@:@

## Deleting here and there

Creating is just the first part. Because memory is not infinite, you may also want to delete a file on your system.

Here, you can use the `delete` method:

@:select(api-style)

@:choice(syntax)

```scala
import cats.effect.{IO, IOApp}

object Deleting extends IOApp.Simple:

  val annoyingFile = Path("desktop/extend_your_car_warranty_now.ad")

  def run: IO[Unit] =
    for
      exists <- annoyingFile.exists
      _ <- if exists then annoyingFile.delete
    yield ()

end Deleting
```

@:choice(static)

```scala
import cats.effect.{IO, IOApp}

object Deleting extends IOApp.Simple:

  val annoyingFile = Path("desktop/extend_your_car_warranty_now.ad")

  def run: IO[Unit] =
    for
      exists <- exists(annoyingFile)
      _ <- if exists then delete(annoyingFile)
    yield ()

end Deleting
```

@:@

Note that we first check if the file exists before deleting it, this is because trying to delete a file with `delete` that does not exist will result in a error. If thats not your style you have two options. One is using the `whenA` combinator from `cats.syntax.applicative.*`:

@:select(api-style)

@:choice(syntax)

```scala
import cats.syntax.applicative.* // You can instead import cats.syntax.all.* !
import cats.effect.{IO, IOApp}

object Deleting extends IOApp.Simple:

  val annoyingFile = Path("desktop/extend_your_car_warranty_now.ad")

  def run: IO[Unit] = annoyingFile.delete.whenA(annoyingFile.exists)

end Deleting
```

@:choice(static)

```scala
import cats.syntax.applicative.* // You can instead import cats.syntax.all.* !
import cats.effect.{IO, IOApp}

object Deleting extends IOApp.Simple:

  val annoyingFile = Path("desktop/extend_your_car_warranty_now.ad")

  def run: IO[Unit] = delete(annoyingFile).whenA(annoyingFile.exists)

end Deleting
```

@:@

Or even better, use the convenience method `deleteIfExists`:

@:select(api-style)

@:choice(syntax)

```scala
import cats.syntax.applicative.* // You can instead import cats.syntax.all.* !
import cats.effect.{IO, IOApp}

object Deleting extends IOApp.Simple:

  val annoyingFile = Path("desktop/extend_your_car_warranty_now.ad")

  def run: IO[Unit] =
    for
      deleted <- annoyingFile.deleteIfExists
      _ <- IO.println(s"Are they reaching out? $deleted")
    yield ()

end Deleting
```

@:choice(static)

```scala
import cats.syntax.applicative.* // You can instead import cats.syntax.all.* !
import cats.effect.{IO, IOApp}

object Deleting extends IOApp.Simple:

  val annoyingFile = Path("desktop/extend_your_car_warranty_now.ad")

  def run: IO[Unit] =
    for
      deleted <- deleteIfExists(annoyingFile)
      _ <- IO.println(s"Are they reaching out? $deleted")
    yield ()

end Deleting
```

@:@

It will return a boolean indicating whether or not the file has been deleted (also deletes directories).

Lastly, you may want to delete not one but multiple files and directories, here is when the `deleteDirectorires` comes handy, as it will delete all the files and directories recursively (similar to `rm -r`):

**Before:**

```
/My files
├── /non empty folder
│   ├── 3751c91b_2024-06-16_7.csv
│   ├── Screenshot 2024-06-16 210523.png
│   ├── /downloaded
│   │   ├── /on_internet
│   │   │   └── Unconfirmed 379466.crdownload
│   │   └── ubuntu-24.04-desktop-amd64.iso
│   └── /spark
│       ├── output0-part-r-00000.nodes
│       └── output1-part-r-00000.nodes
│ 
├── /dont delete
│ 
```

@:select(api-style)

@:choice(syntax)

```scala
import cats.effect.{IO, IOApp}

object Deleting extends IOApp.Simple:

  val nonEmptyFolder = Path("downloads/non empty folder")

  def run: IO[Unit] = nonEmptyFolder.deleteRecursively

end Deleting
```

@:choice(static)

```scala
import cats.effect.{IO, IOApp}

object Deleting extends IOApp.Simple:

  val nonEmptyFolder = Path("downloads/non empty folder")

  def run: IO[Unit] = deleteRecursively(nonEmptyFolder)

end Deleting
```

@:@

**After:**

```
/My files
├── /dont delete
│  
```

### Exercise

You are tired of people abusing the FTP server to upload long pirated movies. Implement a method that checks if a file exceeds a certain size, and if so, automatically deletes it (hint: check out the `size` method!). Return `true` if the file was deleted, `false` otherwise:

@:select(api-style)

@:choice(syntax)

```scala
extension (path: Path) def deleteIfChubby(threshold: Long): IO[Boolean] = ???
```

@:choice(static)

```scala
def deleteIfChubby(path: Path, threshold: Long): IO[Boolean] = ???
```

@:@

## Using temporary files

Maybe you don't want to manually delete a file after its use. This is where temporary files come in to play, as they are deleted automatically.

To create temporary files you have two options, one is to make Cats Effect automatically handle their lifecycle with the `tempFile` and `tempDirectory` methods (useful when you want the files deleted right away), or, if you rather prefer the operating system to take hands in its lifecycle, you can use the `createTempFile` and `createTempDirectory` variants (suitable if you don't care if the files are deleted immediately).

The former takes as a parameter a function that describes how you want to use the file, like this:

@:select(api-style)

@:choice(syntax)

```scala
import cats.syntax.all.*
import cats.effect.{IO, IOApp}

object Temporary extends IOApp.Simple:

  def run: IO[Unit] = tempFile: path =>
    for
      _ <- path.writeLines(LazyList.from('a').map(_.toChar.toString).take(26))
      alphabet <- path.read
      _ <- IO.println("ASCII took hispanics into account!").whenA(alphabet.contains('ñ')) // Not gonna be printed, sadly
    yield ()

end Temporary
```

@:choice(static)

```scala
import cats.syntax.all.*
import cats.effect.{IO, IOApp}

object Temporary extends IOApp.Simple:

  def run: IO[Unit] = tempFile: path =>
    for
      _ <- writeLines(path, LazyList.from('a').map(_.toChar.toString).take(26))
      alphabet <- read(path)
      _ <- IO.println("ASCII took hispanics into account!").whenA(alphabet.contains('ñ')) // Not gonna be printed, sadly
    yield ()

end Temporary
```

@:@

You'll see that the `use` function goes from `Path => IO[A]`, and that `use` basically describes a path that will be used to compute an `A`, with some side effects along the way. When the computation is finished, the file will no longer exist.

The last alternative is with `createTempFile` or `createTempDirectory`. The difference to the one without `create` is that it returns the path where the file was created, something like this:

@:select(api-style)

@:choice(syntax)

```scala
import cats.effect.{IO, IOApp}

object Temporary extends IOApp.Simple:

  val secretPath = Path(".secrets/to_my_secret_lover.txt")

  def run: IO[Unit] = createTempFile.flatMap: path =>
    for
      _ <- path.write("A confesion to my lover: ")
      letter <- secretPath.read
      _ <- path.appendLine(letter)
    yield ()

end Temporary
```

@:choice(static)

```scala
import cats.effect.{IO, IOApp}

object Temporary extends IOApp.Simple:

  val secretPath = Path(".secrets/to_my_secret_lover.txt")

  def run: IO[Unit] = createTempFile.flatMap: path =>
    for
      _ <- write(path,"A confesion to my lover: ")
      letter <- read(secretPath)
      _ <- appendLine(path, letter)
    yield ()

end Temporary
```

@:@

### Exercise

Another really nice way to handle resource lifecycle [is with a `Resource`](https://typelevel.org/cats-effect/docs/std/resource#resource) from Cats Effect. Would you try to be adventurous and implement a third way of handling temporary files with a new function that returns a Cats Effect `Resource`?

```scala
import cats.effect.Resource

def makeTempFile: Resource[IO, Path] = ???
```