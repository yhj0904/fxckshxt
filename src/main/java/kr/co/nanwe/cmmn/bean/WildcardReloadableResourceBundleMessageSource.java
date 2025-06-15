package kr.co.nanwe.cmmn.bean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import kr.co.nanwe.cmmn.util.StringUtil;

public class WildcardReloadableResourceBundleMessageSource extends org.springframework.context.support.ReloadableResourceBundleMessageSource {

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	@Override
	public void setBasenames(String... basenames) {
		
		if (basenames != null) {
			
			List<String> baseNames = new ArrayList<String>();
			
			for (int i = 0; i < basenames.length; i++) {

				String basename = StringUtil.trimToEmpty(basenames[i]);
				if (basename.indexOf("classpath:/") > -1) {
					baseNames.add(basename);
				} else if (StringUtil.isNotBlank(basename)) {
					try {

						Resource[] resources = resourcePatternResolver.getResources(basename);

						for (int j = 0; j < resources.length; j++) {
							Resource resource = resources[j];
							String uri = resource.getURI().toString();
							String baseName = null;

							if (uri.indexOf(".properties") == -1) {
								continue;
							}

							if (resource instanceof FileSystemResource) {
								baseName = "classpath:" + StringUtil.substringBetween(uri, "/classes/", ".properties");
								if(baseName.indexOf("_") > -1 ) {
									baseName = baseName.substring(0, baseName.indexOf("_"));
								}
								baseName = baseName.replaceAll("classpath:", "classpath:/");
								if (baseNames.indexOf(baseName) > -1) {
									continue;
								};

							} else if (resource instanceof ClassPathResource) {
								baseName = StringUtil.substringBefore(uri, ".properties");
								if(baseName.indexOf("_") > -1 ) {
									baseName = baseName.substring(0, baseName.indexOf("_"));
								}
								baseName = baseName.replaceAll("classpath:", "classpath:/");
							} else if (resource instanceof UrlResource) {
								baseName = "classpath:" + StringUtil.substringBetween(uri, ".jar!/", ".properties");
								if(baseName.indexOf("_") > -1 ) {
									baseName = baseName.substring(0, baseName.indexOf("_"));
								}
								baseName = baseName.replaceAll("classpath:", "classpath:/");
							}
							if (baseName != null) {								
								String fullName = processBasename(baseName);
								baseNames.add(fullName);
							}
						}
					} catch (IOException e) {
						logger.debug("No message source files found for basename " + basename + ".");
					}
				}

			}
			logger.debug("EgovWildcardReloadableResourceBundleMessageSource>>basenames>[" + baseNames + "}");			
			super.setBasenames(baseNames.toArray(new String[baseNames.size()]));
		}
	}

	String processBasename(String baseName) {
		String prefix = StringUtil.substringBeforeLast(baseName, "/");
		String name = StringUtil.substringAfterLast(baseName, "/");
		do {
			name = StringUtil.substringBeforeLast(name, "_");
		} while (name.contains("_"));
		return prefix + "/" + name;
	}
}