import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.decompiler.fernflower.FernflowerDecompiler;
import io.github.coolcrabs.brachyura.dependency.JavaJarDependency;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyCollector;
import io.github.coolcrabs.brachyura.fabric.FabricContext.ModDependencyFlag;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.ide.IdeModule;
import io.github.coolcrabs.brachyura.mappings.Namespaces;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.maven.Maven;
import io.github.coolcrabs.brachyura.minecraft.Minecraft;
import io.github.coolcrabs.brachyura.minecraft.VersionMeta;
import io.github.coolcrabs.brachyura.project.java.BuildModule;
import io.github.coolcrabs.brachyura.quilt.QuiltContext;
import io.github.coolcrabs.brachyura.quilt.QuiltMaven;
import io.github.coolcrabs.brachyura.quilt.QuiltModule;
import io.github.coolcrabs.brachyura.quilt.SimpleQuiltProject;
import io.github.coolcrabs.brachyura.util.Lazy;
import io.github.coolcrabs.brachyura.util.PathUtil;
import net.fabricmc.accesswidener.AccessWidenerReader;
import net.fabricmc.accesswidener.AccessWidenerVisitor;
import net.fabricmc.mappingio.tree.MappingTree;

public class Buildscript extends SimpleQuiltProject {
	@Override
	public VersionMeta createMcVersion() {
		return Minecraft.getVersion("1.18.2");
	}

	@Override
	public MappingTree createMappings() {
		return createMojmap();
	}

	@Override
	public FabricLoader getLoader() {
		return new FabricLoader(QuiltMaven.URL, QuiltMaven.loader("0.16.0-beta.16"));
	}

	@Override
	public @Nullable Consumer<AccessWidenerVisitor> getAw() {
		return v -> {
			try {
				new AccessWidenerReader(v).read(Files.newBufferedReader(getResourcesDir().resolve("aura.accesswidener")), Namespaces.NAMED);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		};
	}

	@Override
	public void getModDependencies(ModDependencyCollector d) {
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".qsl.core", "qsl_base", "1.1.0-beta.8+1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".qsl.core", "resource_loader", "1.1.0-beta.8+1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".qsl.core", "lifecycle_events", "1.1.0-beta.8+1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".qsl.core", "registry", "1.1.0-beta.8+1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".qsl.core", "networking", "1.1.0-beta.8+1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".qsl.item", "item_group", "1.1.0-beta.8+1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".qsl.gui", "tooltip", "1.1.0-beta.8+1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);

		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api", "fabric-api-base", "1.0.0-beta.13+0.51.1-1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api", "fabric-networking-api-v1", "1.0.0-beta.13+0.51.1-1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api", "fabric-resource-loader-v0", "1.0.0-beta.13+0.51.1-1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api", "fabric-screen-api-v1", "1.0.0-beta.13+0.51.1-1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api", "fabric-registry-sync-v0", "1.0.0-beta.13+0.51.1-1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api", "fabric-data-generation-api-v1", "1.0.0-beta.13+0.51.1-1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api", "fabric-lifecycle-events-v1", "1.0.0-beta.13+0.51.1-1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		d.addMaven(QuiltMaven.URL, new MavenId(QuiltMaven.GROUP_ID + ".quilted-fabric-api", "fabric-rendering-v1", "1.0.0-beta.13+0.51.1-1.18.2"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
		jij(d.addMaven("https://ladysnake.jfrog.io/artifactory/mods/", new MavenId("dev.onyxstudios.cardinal-components-api:cardinal-components-base:4.1.4"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE));
		jij(d.addMaven("https://ladysnake.jfrog.io/artifactory/mods/", new MavenId("dev.onyxstudios.cardinal-components-api:cardinal-components-entity:4.1.4"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE));
		jij(d.addMaven("https://maven.quiltmc.org/repository/release/", new MavenId("org.quiltmc:quilt-json5:1.0.0"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE));
		jij(d.addMaven("https://maven.gegy.dev/", new MavenId("dev.lambdaurora:spruceui:3.3.1+1.17"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME));
		d.addMaven("https://maven.terraformersmc.com/releases/", new MavenId("com.terraformersmc:modmenu:3.2.1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);

		d.addMaven("https://api.modrinth.com/maven/", new MavenId("maven.modrinth:lazydfu:0.1.2"), ModDependencyFlag.RUNTIME);
		d.addMaven("https://api.modrinth.com/maven/", new MavenId("maven.modrinth:lithium:mc1.18.2-0.7.9"), ModDependencyFlag.RUNTIME);
	}

	@Override
	public @Nullable BrachyuraDecompiler decompiler() {
		return new FernflowerDecompiler(Maven.getMavenJarDep(QuiltMaven.URL, new MavenId("org.quiltmc:quiltflower:1.8.1")));
	}

	@Override
	public int getJavaVersion() {
		return 17;
	}

	public Path[] getResourcesDirs() {
		return new Path[]{super.getResourceDirs()[0], getProjectDir().resolve("src").resolve("generated").resolve("resources")};
	}

	@Override
	public Path getBuildJarPath() {
		return getBuildLibsDir().resolve(String.format("%s-mc-%s-%s.jar", getModId(), createMcVersion().version, getVersion()));
	}

	@Override
	protected QuiltModule createModule() {
		return new SimpleQuiltModule((QuiltContext)context.get()) {
			@Override
			public IdeModule ideModule() {
				Path cwd = PathUtil.resolveAndCreateDir(getModuleRoot(), "run");
				Lazy<List<Path>> classpath = new Lazy<>(() -> {
					Path mappingsClasspath = writeMappings4FabricStuff().getParent().getParent();
					ArrayList<Path> r = new ArrayList<>(context.runtimeDependencies.get().size() + 1);
					for (JavaJarDependency dependency : context.runtimeDependencies.get()) {
						r.add(dependency.jar);
					}
					r.add(mappingsClasspath);
					return r;
				});
				return new IdeModule.IdeModuleBuilder()
					.name(getModuleName())
					.root(getModuleRoot())
					.javaVersion(getJavaVersion())
					.dependencies(context.ideDependencies)
					.sourcePath(getSrcDir())
					.resourcePaths(getResourcesDirs())
					.dependencyModules(getModuleDependencies().stream().map(BuildModule::ideModule).collect(Collectors.toList()))
					.runConfigs(
						new IdeModule.RunConfigBuilder()
							.name("Minecraft Client")
							.cwd(cwd)
							.mainClass("net.fabricmc.loader.launch.knot.KnotClient")
							.classpath(classpath)
							.resourcePaths(getResourcesDirs())
							.vmArgs(() -> this.ideVmArgs(true))
							.args(() -> this.ideArgs(true)),
						new IdeModule.RunConfigBuilder()
							.name("Minecraft Server")
							.cwd(cwd)
							.mainClass("net.fabricmc.loader.launch.knot.KnotServer")
							.classpath(classpath)
							.resourcePaths(getResourcesDirs())
							.vmArgs(() -> this.ideVmArgs(false))
							.args(() -> this.ideArgs(false)),
						new IdeModule.RunConfigBuilder()
							.name("Data Generation Client")
							.cwd(cwd)
							.mainClass("net.fabricmc.loader.launch.knot.KnotClient")
							.classpath(classpath)
							.resourcePaths(getResourcesDirs())
							.vmArgs(() -> {
								List<String> r = this.ideVmArgs(true);
								r.addAll(Arrays.asList(
									"-Dfabric-api.datagen",
									String.format("-Dfabric-api.datagen.output-dir=%s", getProjectDir().resolve("src").resolve("generated").resolve("resources")),
									"-Dfabric-api.datagen.strict-validation"
								));
								return r;
							})
							.args(() -> this.ideArgs(true)),
						new IdeModule.RunConfigBuilder()
							.name("Data Generation Server")
							.cwd(cwd)
							.mainClass("net.fabricmc.loader.launch.knot.KnotServer")
							.classpath(classpath)
							.resourcePaths(getResourcesDirs())
							.vmArgs(() -> {
								List<String> r = this.ideVmArgs(false);
								r.addAll(Arrays.asList(
									"-Dfabric-api.datagen",
									String.format("-Dfabric-api.datagen.output-dir=%s", getProjectDir().resolve("src").resolve("generated").resolve("resources")),
									"-Dfabric-api.datagen.strict-validation"
								));
								return r;
							})
						.args(() -> this.ideArgs(false))
				)
				.build();
			};
		};
	}
}
