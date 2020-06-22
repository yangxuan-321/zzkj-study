
scalaVersion := "2.13.2" // Also supports 2.12.x

val http4sV 			 = "0.21.2"
val prometheusV    = "0.8.0"
val tsecV          = "0.2.0-M2"
val catsV          = "2.1.0"
val doobieV        = "0.8.8"
val akkaV          = "2.6.5"

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")


// http4s 相关
libraryDependencies ++= Seq(
	"org.http4s"         %% "http4s-blaze-server"       % http4sV,
	"org.http4s"         %% "http4s-blaze-client"       % http4sV,
	"org.http4s"         %% "http4s-circe"              % http4sV,
	"org.http4s"         %% "http4s-dsl"                % http4sV,
	"org.http4s"         %% "http4s-twirl"              % http4sV,
	"org.http4s"         %% "http4s-prometheus-metrics" % http4sV,
	"org.http4s"         %% "http4s-async-http-client"  % http4sV,
)

// 监控相关
libraryDependencies ++= Seq(
	"io.prometheus"   					% "simpleclient"              		% prometheusV,
	"io.prometheus"   					% "simpleclient_hotspot"      		% prometheusV,
	"io.prometheus"   					% "simpleclient_common"       		% prometheusV
)

// 配置相关
libraryDependencies ++= Seq(
	"com.github.pureconfig"      %% "pureconfig"                	% "0.12.1",
)

// auth 相关
libraryDependencies ++= Seq(
	"io.github.jmcardon" %% "tsec-common"               % tsecV,
	"io.github.jmcardon" %% "tsec-password"             % tsecV,
	"io.github.jmcardon" %% "tsec-cipher-jca"           % tsecV,
	"io.github.jmcardon" %% "tsec-cipher-bouncy"        % tsecV,
	"io.github.jmcardon" %% "tsec-mac"                  % tsecV,
	"io.github.jmcardon" %% "tsec-signatures"           % tsecV,
	"io.github.jmcardon" %% "tsec-hash-jca"             % tsecV,
	"io.github.jmcardon" %% "tsec-hash-bouncy"          % tsecV,
	"io.github.jmcardon" %% "tsec-jwt-mac"              % tsecV,
	"io.github.jmcardon" %% "tsec-jwt-sig"              % tsecV,
	"io.github.jmcardon" %% "tsec-http4s"               % tsecV
)

// scalapb支持
PB.targets in Compile := Seq(
	scalapb.gen() -> (sourceManaged in Compile).value / "scalapb"
)
libraryDependencies += "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"

// cats支持
libraryDependencies ++= Seq(
	"org.typelevel"     %% "cats-core"            % catsV,
	"org.typelevel"     %% "cats-free"            % catsV
)

// 日志
libraryDependencies ++= Seq(
	"com.typesafe.scala-logging" %% "scala-logging"             % "3.9.2"
)

// 数据库
libraryDependencies ++= Seq(
	"org.tpolecat"               %% "doobie-core"               % doobieV,
	"org.tpolecat"               %% "doobie-hikari"             % doobieV,
	"org.tpolecat"               %% "doobie-postgres"           % doobieV
)

// akka
libraryDependencies ++= Seq(
	"com.typesafe.akka"  				%% "akka-actor"                	% akkaV,
	"com.typesafe.akka"  				%% "akka-stream"               	% akkaV,
	"com.typesafe.akka"  				%% "akka-actor-typed"          	% akkaV,
	"com.typesafe.akka" 				%% "akka-cluster"               % akkaV,
	"com.typesafe.akka" 				%% "akka-cluster-tools"         % akkaV,
	"com.typesafe.akka" 				%% "akka-cluster-metrics"       % akkaV,
	"com.typesafe.akka" 				%% "akka-cluster-typed"         % akkaV
)