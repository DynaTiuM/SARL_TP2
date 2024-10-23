package io.sarl.demos.basic.holarchy;

import com.google.common.base.Objects;
import io.sarl.api.core.Behaviors;
import io.sarl.api.core.DefaultContextInteractions;
import io.sarl.api.core.Initialize;
import io.sarl.api.core.InnerContextAccess;
import io.sarl.api.core.Lifecycle;
import io.sarl.api.core.Logging;
import io.sarl.api.core.MemberLeft;
import io.sarl.api.core.ParticipantJoined;
import io.sarl.api.core.Schedules;
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
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Pure;

/**
 * Holon architecture used to create a whole self-similar hierarchy of agent
 * @author <a href="http://www.ciad-lab.fr/nicolas_gaud">Nicolas Gaud</a>
 */
@SarlSpecification("0.13")
@SarlElementType(19)
@SuppressWarnings("all")
public class SearchAgent extends Agent {
  private File directory;

  private String criteria = ".sarl";

  private UUID parent;

  private ConcurrentHashMap<UUID, File> map = new ConcurrentHashMap<UUID, File>();

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    this.parent = occurrence.spawner;
    boolean _isEmpty = ((List<Object>)Conversions.doWrapArray(occurrence.parameters)).isEmpty();
    if (_isEmpty) {
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.setLoggingName("ROOT AGENT");
    }
  }

  private void $behaviorUnit$MemberLeft$1(final MemberLeft occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info(("map : " + this.map));
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info(("A member left : " + occurrence.agentID));
    UUID childID = occurrence.agentID;
    boolean _containsKey = this.map.containsKey(childID);
    if (_containsKey) {
      this.map.remove(childID);
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      int _size = this.map.size();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_2.info(("Child removed. Remaining children: " + Integer.valueOf(_size)));
      int _size_1 = this.map.size();
      if ((_size_1 <= 0)) {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_3 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_3.info("No more children: killing myself.");
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
        Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
        _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.killMe();
      }
    }
  }

  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$MemberLeft$1(final MemberLeft it, final MemberLeft occurrence) {
    boolean _containsKey = this.map.containsKey(occurrence.agentID);
    return _containsKey;
  }

  private void $behaviorUnit$ParticipantJoined$2(final ParticipantJoined occurrence) {
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
  private boolean $behaviorUnitGuard$ParticipantJoined$2(final ParticipantJoined it, final ParticipantJoined occurrence) {
    return (((!(it != null && this.getID().equals(it.getSource().getID()))) && this.map.containsKey(occurrence.getSource().getID())) && Objects.equal(occurrence.spaceID, this.$CAPACITY_USE$IO_SARL_API_CORE_INNERCONTEXTACCESS$CALLER().getInnerContext().getDefaultSpace().getSpaceID()));
  }

  private void $behaviorUnit$SearchRequest$3(final SearchRequest occurrence) {
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
      return Boolean.valueOf((it.isFile() && it.getName().contains(this.criteria)));
    };
    Iterable<File> _filter_1 = IterableExtensions.<File>filter(IterableExtensions.<File>toList(((Iterable<File>)Conversions.doWrapArray(this.directory.listFiles()))), _function_1);
    for (final File subfile : _filter_1) {
      {
        Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
        String _name = subfile.getName();
        _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info((_name + " est un fichier"));
        String _name_1 = subfile.getName();
        File fileFound = new File(this.directory, _name_1);
        DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
        FileFound _fileFound = new FileFound(fileFound);
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
    int _size = this.map.size();
    if ((_size <= 0)) {
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info("No more tasks: killing myself.");
      Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.killMe();
    }
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
    boolean _isEmpty = this.map.isEmpty();
    if (_isEmpty) {
      Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info("No more tasks: killing myself.");
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
  @ImportedCapacityFeature(Schedules.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES;

  @SyntheticMember
  @Pure
  private Schedules $CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES == null || this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES = $getSkill(Schedules.class);
    }
    return $castSkill(Schedules.class, this.$CAPACITY_USE$IO_SARL_API_CORE_SCHEDULES);
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

  @Extension
  @ImportedCapacityFeature(Behaviors.class)
  @SyntheticMember
  private transient AtomicSkillReference $CAPACITY_USE$IO_SARL_API_CORE_BEHAVIORS;

  @SyntheticMember
  @Pure
  private Behaviors $CAPACITY_USE$IO_SARL_API_CORE_BEHAVIORS$CALLER() {
    if (this.$CAPACITY_USE$IO_SARL_API_CORE_BEHAVIORS == null || this.$CAPACITY_USE$IO_SARL_API_CORE_BEHAVIORS.get() == null) {
      this.$CAPACITY_USE$IO_SARL_API_CORE_BEHAVIORS = $getSkill(Behaviors.class);
    }
    return $castSkill(Behaviors.class, this.$CAPACITY_USE$IO_SARL_API_CORE_BEHAVIORS);
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
  private void $guardEvaluator$MemberLeft(final MemberLeft occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$MemberLeft$1(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$MemberLeft$1(occurrence));
    }
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$FileFound(final FileFound occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$FileFound$4(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SearchRequest(final SearchRequest occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SearchRequest$3(occurrence));
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ParticipantJoined(final ParticipantJoined occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$ParticipantJoined$2(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ParticipantJoined$2(occurrence));
    }
  }

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SearchFinished(final SearchFinished occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SearchFinished$5(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(Initialize.class);
    toBeFilled.add(MemberLeft.class);
    toBeFilled.add(ParticipantJoined.class);
    toBeFilled.add(FileFound.class);
    toBeFilled.add(SearchFinished.class);
    toBeFilled.add(SearchRequest.class);
  }

  @SyntheticMember
  @Override
  public boolean $isSupportedEvent(final Class<? extends Event> event) {
    if (Initialize.class.isAssignableFrom(event)) {
      return true;
    }
    if (MemberLeft.class.isAssignableFrom(event)) {
      return true;
    }
    if (ParticipantJoined.class.isAssignableFrom(event)) {
      return true;
    }
    if (FileFound.class.isAssignableFrom(event)) {
      return true;
    }
    if (SearchFinished.class.isAssignableFrom(event)) {
      return true;
    }
    if (SearchRequest.class.isAssignableFrom(event)) {
      return true;
    }
    return false;
  }

  @SyntheticMember
  @Override
  public void $evaluateBehaviorGuards(final Object event, final Collection<Runnable> callbacks) {
    super.$evaluateBehaviorGuards(event, callbacks);
    if (event instanceof Initialize) {
      final Initialize occurrence = (Initialize) event;
      $guardEvaluator$Initialize(occurrence, callbacks);
    }
    if (event instanceof MemberLeft) {
      final MemberLeft occurrence = (MemberLeft) event;
      $guardEvaluator$MemberLeft(occurrence, callbacks);
    }
    if (event instanceof ParticipantJoined) {
      final ParticipantJoined occurrence = (ParticipantJoined) event;
      $guardEvaluator$ParticipantJoined(occurrence, callbacks);
    }
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
