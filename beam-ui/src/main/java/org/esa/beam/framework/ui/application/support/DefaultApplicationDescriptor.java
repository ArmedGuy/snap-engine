package org.esa.beam.framework.ui.application.support;

import com.bc.ceres.core.CoreException;
import com.bc.ceres.core.runtime.ConfigurableExtension;
import com.bc.ceres.core.runtime.ConfigurationElement;
import com.bc.ceres.core.runtime.Module;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.converters.SingleValueConverterWrapper;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import org.esa.beam.framework.ui.application.ApplicationDescriptor;

import java.util.ArrayList;

public class DefaultApplicationDescriptor implements ConfigurableExtension, ApplicationDescriptor {

    private String displayName;
    private String applicationId;

    @XStreamAlias("resourceBundle")
    private String resourceBundleName;

    @XStreamAlias("frameIcon")
    private String frameIconPath;

    @XStreamAlias("image")
    private String imagePath;

    private ArrayList<ID> excludedActions;
    private ArrayList<ID> excludedToolViews;

    private transient String copyright;
    private transient String symbolicName;
    private transient String loggerName;
    private transient String version;

    public String getApplicationId() {
        return applicationId;
    }

    public String getCopyright() {
        return copyright;
    }

    public String getResourceBundleName() {
        return resourceBundleName;
    }

    public String getSymbolicName() {
        return symbolicName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getVersion() {
        return version;
    }

    public String getFrameIconPath() {
        return frameIconPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String[] getExcludedActions() {
        return toStringArray(excludedActions);
    }

    public String[] getExcludedToolViews() {
        return toStringArray(excludedToolViews);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setResourceBundleName(String resourceBundleName) {
        this.resourceBundleName = resourceBundleName;
    }

    public void setFrameIconPath(String frameIconPath) {
        this.frameIconPath = frameIconPath;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setSymbolicName(String symbolicName) {
        this.symbolicName = symbolicName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Configures this extension with the supplied configuration data.
     *
     * @param config The configuration data.
     * @throws com.bc.ceres.core.CoreException
     *          if an error occured during configuration.
     */
    public void configure(ConfigurationElement config) throws CoreException {
        if (applicationId == null) {
            throw new CoreException(String.format("Missing configuration element 'applicationId' in element '%s'.", config.getName()));
        }

        Module declaringModule = config.getDeclaringExtension().getDeclaringModule();
        if (displayName == null) {
            displayName = declaringModule.getName();
        }
        if (symbolicName == null) {
            symbolicName = declaringModule.getSymbolicName();
        }
        if (loggerName == null) {
            loggerName = declaringModule.getSymbolicName();
        }
        if (version == null || "${beam.build.id}".equals(version)) {
            version = declaringModule.getVersion().toString();
        }
        if (copyright == null) {
            copyright = declaringModule.getCopyright();
        }
        if (frameIconPath == null) {
            frameIconPath = "/org/esa/beam/resources/images/icons/BeamIcon24.png";
        }
        if (imagePath == null) {
            imagePath = "/org/esa/beam/resources/images/about.jpg";
        }
    }
    private String[] toStringArray(ArrayList<ID> idList) {
        if (idList == null) {
            return new String[0];
        }
        final String[] strings = new String[idList.size()];
        for (int i = 0; i < idList.size(); i++) {
            ID id = idList.get(i);
            strings[i] = id.id;
        }
        return strings;
    }

    @XStreamAlias("id")
    @XStreamConverter(IDC.class)
    public static class ID {
        private String id;

        private ID(String id) {
            this.id = id;
        }
    }

    public static class IDC extends SingleValueConverterWrapper {
        public IDC() {
            super(new IDSVC());
        }
    }


    public static class IDSVC extends AbstractSingleValueConverter {
        @Override
        public boolean canConvert(Class type) {
            return type == ID.class;
        }

        @Override
        public Object fromString(String str) {
            return new ID(str);
        }
    }
}
