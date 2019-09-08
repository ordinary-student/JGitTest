package jgit;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/*
 * JGit工具类
 */
public class JGitUtil
{
	// 设置远程服务器上的用户名和密码
	public UsernamePasswordCredentialsProvider usernamePasswordCredentialsProvider = new UsernamePasswordCredentialsProvider(
			"username", "password");

	// 远程仓库路径
	public String remotePath = "https://github.com/ordinary-student/JGitTest.git";
	// 本地仓库路径
	public String localPath = "D:\\JAVA\\works\\JGitTest";
	// 新建仓库时，存放新仓库的本地路径
	public String initPath = "D:\\JAVA\\works\\JGitTest";

	/**
	 * 克隆远程仓库到本地
	 * 
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public void cloneRepository() throws IOException, GitAPIException
	{
		// 克隆代码库命令
		CloneCommand cloneCommand = Git.cloneRepository();
		// 待克隆的远程仓库地址
		Git git = cloneCommand.setURI(remotePath).setBranch("master") // 设置要clone下来的分支
				.setDirectory(new File(localPath)) // 设置下载存放路径
				.setCredentialsProvider(usernamePasswordCredentialsProvider) // 设置权限验证
				.call();

		System.out.print(git.tag());
	}

	/**
	 * 本地新建仓库
	 */
	public void create() throws IOException
	{
		// 本地新建仓库地址
		Repository newRepo = FileRepositoryBuilder.create(new File(initPath + "/.git"));
		newRepo.create();
	}

	/**
	 * 本地仓库新增文件
	 */
	public void addFile() throws IOException, GitAPIException
	{
		// 新增文件
		File myfile = new File(localPath + "/test.txt");
		myfile.createNewFile();
		// git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));
		// 添加文件
		git.add().addFilepattern("test").call();
		// 关闭
		git.close();
	}

	/**
	 * 提交代码到本地仓库
	 */
	public void commit() throws IOException, GitAPIException, JGitInternalException
	{
		// git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));
		// 提交代码
		git.commit().setMessage("test jGit").call();
		// 关闭
		git.close();
	}

	/**
	 * 拉取远程仓库内容到本地
	 */
	public void testPull() throws IOException, GitAPIException
	{
		// git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));
		// 设置要拉取的分支和权限验证
		git.pull().setRemoteBranchName("master").setCredentialsProvider(usernamePasswordCredentialsProvider).call();
		// 关闭
		git.close();
	}

	/**
	 * push本地代码到远程仓库
	 */
	public void testPush() throws IOException, JGitInternalException, GitAPIException
	{
		// git仓库地址
		Git git = new Git(new FileRepository(localPath + "/.git"));
		// 获取状态
		Iterable iterable = git.push().setCredentialsProvider(usernamePasswordCredentialsProvider).call();
		PushResult pushResult = (PushResult) iterable.iterator().next();
		org.eclipse.jgit.transport.RemoteRefUpdate.Status status = pushResult.getRemoteUpdate("refs/heads/master")
				.getStatus();

		System.out.println(status.toString());

		// 关闭
		git.close();
	}
}
