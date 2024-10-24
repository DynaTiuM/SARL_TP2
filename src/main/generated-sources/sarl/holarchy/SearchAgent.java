package holarchy;

import com.google.common.base.Objects;
import io.sarl.api.core.DefaultContextInteractions;
import io.sarl.api.core.Destroy;
import io.sarl.api.core.Initialize;
import io.sarl.api.core.InnerContextAccess;
import io.sarl.api.core.Lifecycle;
import io.sarl.api.core.Logging;
import io.sarl.api.core.ParticipantJoined;
import io.sarl.lang.core.Address;
import io.sarl.lang.core.Agent;
import io.sarl.lang.core.AtomicSkillReference;
import io.sarl.lang.core.DynamicSkillProvider;
import io.sarl.lang.core.Event;
import io.sarl.lang.core.Scope;
import io.sarl.lang.core.annotation.ImportedCapacityFeature;
import io.sarl.lang.core.annotation.PerceptGuardEvaluator;
import io.sarl.lang.core.annotation.SarlElementType;
import io.sarl.lang.core.annotation.SarlSpecification;
import io.sarl.lang.core.annotation.SyntheticMember;
import io.sarl.lang.core.util.SerializableProxy;
import java.io.File;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(19)
@SuppressWarnings("all")
public class SearchAgent extends Agent {
  private File directory;

  private String criteria;

  private UUID parent;

  private ConcurrentHashMap<UUID, File> map = new ConcurrentHashMap<UUID, File>();

  private AtomicBoolean isSearchFinished;

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    this.parent = occurrence.spawner;
    AtomicBoolean _atomicBoolean = new AtomicBoolean(false);
    this.isSearchFinished = _atomicBoolean;
    boolean _isEmpty = ((List<Object>)Conversions.doWrapArray(occurrence.parameters)).isEmpty();
    if ((!_isEmpty)) {
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.setLoggingName("ROOT AGENT");
    }
  }

  private void $behaviorUnit$ParticipantJoined$1(final ParticipantJoined occurrence) {
    InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
    File _get = this.map.get(occurrence.getSource().getID());
    SearchRequest _searchRequest = new SearchRequest(_get, this.criteria);
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID $_iD;
      
      public $SerializableClosureProxy(final UUID $_iD) {
        this.$_iD = $_iD;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, $_iD);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        UUID _iD_1 = occurrence.getSource().getID();
        return Objects.equal(_iD, _iD_1);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, occurrence.getSource().getID());
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext().getDefaultSpace().emit(occurrence.getSource().getID(), _searchRequest, _function);
  }

  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$ParticipantJoined$1(final ParticipantJoined it, final ParticipantJoined occurrence) {
    return (((!(it != null && this.getID().equals(it.getSource().getID()))) && this.map.containsKey(occurrence.getSource().getID())) && Objects.equal(occurrence.spaceID, this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER().getInnerContext().getDefaultSpace().getSpaceID()));
  }

  private void $behaviorUnit$SearchRequest$2(final SearchRequest occurrence) {
    this.criteria = occurrence.criteria;
    this.directory = occurrence.path;
    boolean _exists = this.directory.exists();
    if ((!_exists)) {
      throw new IllegalArgumentException("The directory doesn\'t exist!");
    }
    final Function1<File, Boolean> _function = (File it) -> {
      return Boolean.valueOf(it.isDirectory());
    };
    Iterable<File> _filter = IterableExtensions.<File>filter(IterableExtensions.<File>toList(((Iterable<File>)Conversions.doWrapArray(this.directory.listFiles()))), _function);
    for (final File subdir : _filter) {
      {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        String _name = subdir.getName();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info((_name + " est un dossier."));
        final UUID aid = UUID.randomUUID();
        String _string = subdir.toString();
        File _file = new File(_string);
        this.map.put(aid, _file);
        Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
        InnerContextAccess _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.spawnInContextWithID(SearchAgent.class, aid, _$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER.getInnerContext());
      }
    }
    final Function1<File, Boolean> _function_1 = (File it) -> {
      return Boolean.valueOf((it.isFile() && it.getName().endsWith(this.criteria)));
    };
    Iterable<File> _filter_1 = IterableExtensions.<File>filter(IterableExtensions.<File>toList(((Iterable<File>)Conversions.doWrapArray(this.directory.listFiles()))), _function_1);
    for (final File subfile : _filter_1) {
      {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        String _name = subfile.getName();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info((_name + " est un fichier"));
        String _name_1 = subfile.getName();
        File foundFile = new File(this.directory, _name_1);
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        FileFound _fileFound = new FileFound(foundFile);
        class $SerializableClosureProxy implements Scope<Address> {
          
          private final UUID $_iD;
          
          public $SerializableClosureProxy(final UUID $_iD) {
            this.$_iD = $_iD;
          }
          
          @Override
          public boolean matches(final Address it) {
            UUID _iD = it.getID();
            return Objects.equal(_iD, $_iD);
          }
        }
        final Scope<Address> _function_2 = new Scope<Address>() {
          @Override
          public boolean matches(final Address it) {
            UUID _iD = it.getID();
            UUID _iD_1 = occurrence.getSource().getID();
            return Objects.equal(_iD, _iD_1);
          }
          private Object writeReplace() throws ObjectStreamException {
            return new SerializableProxy($SerializableClosureProxy.class, occurrence.getSource().getID());
          }
        };
        _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_fileFound, _function_2);
      }
    }
    this.isSearchFinished.set(true);
    int _size = this.map.size();
    if ((_size <= 0)) {
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info("No more tasks: killing myself.");
      Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.killMe();
    }
  }

  private void $behaviorUnit$Destroy$3(final Destroy occurrence) {
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    SearchFinished _searchFinished = new SearchFinished();
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID $_parent;
      
      public $SerializableClosureProxy(final UUID $_parent) {
        this.$_parent = $_parent;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, $_parent);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, SearchAgent.this.parent);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, SearchAgent.this.parent);
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_searchFinished, _function);
  }

  private void $behaviorUnit$FileFound$4(final FileFound occurrence) {
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    FileFound _fileFound = new FileFound(occurrence.path);
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID $_parent;
      
      public $SerializableClosureProxy(final UUID $_parent) {
        this.$_parent = $_parent;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, $_parent);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, SearchAgent.this.parent);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, SearchAgent.this.parent);
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_fileFound, _function);
  }

  private void $behaviorUnit$SearchFinished$5(final SearchFinished occurrence) {
    UUID childID = occurrence.getSource().getID();
    synchronized (childID) {
      boolean _containsKey = this.map.containsKey(childID);
      if (_containsKey) {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(("map contains : " + childID));
        this.map.remove(childID);
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        int _size = this.map.size();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info(("Child removed. Remaining children: " + Integer.valueOf(_size)));
        if (((this.map.size() <= 0) && this.isSearchFinished.get())) {
          Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2.info("No more children: killing myself.");
          Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
          _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.killMe();
        }
      }
    }
  }

  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$SearchFinished$5(final SearchFinished it, final SearchFinished occurrence) {
    boolean _containsKey = this.map.containsKey(occurrence.getSource().getID());
    return _containsKey;
  }

  @Extension
  @ImportedCapacityFeature(Lifecycle.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE;

  @SyntheticMember
  @Pure
  private Lifecycle $CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE = $getSkill(Lifecycle.class);
    }
    return $castSkill(Lifecycle.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE);
  }

  @Extension
  @ImportedCapacityFeature(InnerContextAccess.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS;

  @SyntheticMember
  @Pure
  private InnerContextAccess $CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS = $getSkill(InnerContextAccess.class);
    }
    return $castSkill(InnerContextAccess.class, this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS);
  }

  @Extension
  @ImportedCapacityFeature(DefaultContextInteractions.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS;

  @SyntheticMember
  @Pure
  private DefaultContextInteractions $CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS = $getSkill(DefaultContextInteractions.class);
    }
    return $castSkill(DefaultContextInteractions.class, this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS);
  }

  @Extension
  @ImportedCapacityFeature(Logging.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_LOGGING;

  @SyntheticMember
  @Pure
  private Logging $CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING == null || this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING = $getSkill(Logging.class);
    }
    return $castSkill(Logging.class, this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING);
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Destroy(final Destroy occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Destroy$3(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SearchFinished(final SearchFinished occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$SearchFinished$5(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SearchFinished$5(occurrence));
    }
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ParticipantJoined(final ParticipantJoined occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$ParticipantJoined$1(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ParticipantJoined$1(occurrence));
    }
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SearchRequest(final SearchRequest occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SearchRequest$2(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$FileFound(final FileFound occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$FileFound$4(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(FileFound.class);
    toBeFilled.add(SearchFinished.class);
    toBeFilled.add(SearchRequest.class);
    toBeFilled.add(Destroy.class);
    toBeFilled.add(Initialize.class);
    toBeFilled.add(ParticipantJoined.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (FileFound.class.isAssignableFrom(event)) {
      return true;
    }
    if (SearchFinished.class.isAssignableFrom(event)) {
      return true;
    }
    if (SearchRequest.class.isAssignableFrom(event)) {
      return true;
    }
    if (Destroy.class.isAssignableFrom(event)) {
      return true;
    }
    if (Initialize.class.isAssignableFrom(event)) {
      return true;
    }
    if (ParticipantJoined.class.isAssignableFrom(event)) {
      return true;
    }
    return false;
  }

  @SyntheticMember
  @Override
  public void $evaluateBehaviorGuards(final Object event, final Collection<Runnable> callbacks) {
    super.$evaluateBehaviorGuards(event, callbacks);
    if (event instanceof FileFound) {
      final FileFound occurrence = (FileFound) event;
      $guardEvaluator$FileFound(occurrence, callbacks);
    }
    if (event instanceof SearchFinished) {
      final SearchFinished occurrence = (SearchFinished) event;
      $guardEvaluator$SearchFinished(occurrence, callbacks);
    }
    if (event instanceof SearchRequest) {
      final SearchRequest occurrence = (SearchRequest) event;
      $guardEvaluator$SearchRequest(occurrence, callbacks);
    }
    if (event instanceof Destroy) {
      final Destroy occurrence = (Destroy) event;
      $guardEvaluator$Destroy(occurrence, callbacks);
    }
    if (event instanceof Initialize) {
      final Initialize occurrence = (Initialize) event;
      $guardEvaluator$Initialize(occurrence, callbacks);
    }
    if (event instanceof ParticipantJoined) {
      final ParticipantJoined occurrence = (ParticipantJoined) event;
      $guardEvaluator$ParticipantJoined(occurrence, callbacks);
    }
  }

  @Override
  @Pure
  @SyntheticMember
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    SearchAgent other = (SearchAgent) obj;
    if (!java.util.Objects.equals(this.criteria, other.criteria))
      return false;
    if (!java.util.Objects.equals(this.parent, other.parent))
      return false;
    return super.equals(obj);
  }

  @Override
  @Pure
  @SyntheticMember
  public int hashCode() {
    int result = super.hashCode();
    final int prime = 31;
    result = prime * result + java.util.Objects.hashCode(this.criteria);
    result = prime * result + java.util.Objects.hashCode(this.parent);
    return result;
  }

  @SyntheticMember
  public SearchAgent(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public SearchAgent(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
