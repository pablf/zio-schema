package zio.schema

import scala.language.experimental.macros
import scala.reflect.macros.whitebox

/**
   * Useful to create factory methods.
   * 
   * import Factory._
   * def createSomeTrait[A: Factory](deriver: Deriver[SomeTrait])(implicit schema: Schema[A]): SomeTrait[A] = 
   *    implicitly[Factory[A]].derive[SomeTrait](deriver)
   * 
   */
trait Factory[A] {
  def derive[F[_]](deriver: Deriver[F])(implicit schema: Schema[A]): F[A]
}

object Factory {

  implicit def factory[A]: Factory[A] = macro factoryImpl[A]

  def factoryImpl[A](
    c: whitebox.Context
  ): c.Tree = {
    import c.universe._

    q"""
        new Factory[A] {
            override def derive[F[_]](deriver: Deriver[F])(implicit schema: Schema[A]): F[A] = Derive.derive[F, A](deriver)
                
        }
    
    """
  }
}