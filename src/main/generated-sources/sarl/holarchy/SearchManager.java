package holarchy;

import com.google.common.base.Objects;
import io.sarl.api.core.DefaultContextInteractions;
import io.sarl.api.core.Initialize;
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
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.inject.Inject;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Pure;

@SarlSpecification("0.13")
@SarlElementType(19)
@SuppressWarnings("all")
public class SearchManager extends Agent {
  private File rootPath;

  private String criteria;

  private UUID aid;

  private ConcurrentLinkedQueue<File> paths;

  private SearchResultCallback resultListener;

  private void $behaviorUnit$Initialize$0(final Initialize occurrence) {
    synchronized (this) {
      Object _get = occurrence.parameters[0];
      File _file = new File((_get == null ? null : _get.toString()));
      this.rootPath = _file;
      Object _get_1 = occurrence.parameters[1];
      this.criteria = (_get_1 == null ? null : _get_1.toString());
      Object _get_2 = occurrence.parameters[2];
      this.resultListener = ((SearchResultCallback) _get_2);
      this.aid = UUID.randomUUID();
      ConcurrentLinkedQueue<File> _concurrentLinkedQueue = new ConcurrentLinkedQueue<File>();
      this.paths = _concurrentLinkedQueue;
      Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
      DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
      _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.spawnInContextWithID(SearchAgent.class, this.aid, _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.getDefaultContext(), Boolean.valueOf(true));
    }
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.setLoggingName("Search Manager");
  }

  private void $behaviorUnit$ParticipantJoined$1(final ParticipantJoined occurrence) {
    DefaultContextInteractions _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER();
    SearchRequest _searchRequest = new SearchRequest(this.rootPath, this.criteria);
    class $SerializableClosureProxy implements Scope<Address> {
      
      private final UUID $_aid;
      
      public $SerializableClosureProxy(final UUID $_aid) {
        this.$_aid = $_aid;
      }
      
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, $_aid);
      }
    }
    final Scope<Address> _function = new Scope<Address>() {
      @Override
      public boolean matches(final Address it) {
        UUID _iD = it.getID();
        return Objects.equal(_iD, SearchManager.this.aid);
      }
      private Object writeReplace() throws ObjectStreamException {
        return new SerializableProxy($SerializableClosureProxy.class, SearchManager.this.aid);
      }
    };
    _$CAPACITY_USE$IO_SARL_API_CORE_DEFAULTCONTEXTINTERACTIONS$CALLER.emit(_searchRequest, _function);
  }

  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$ParticipantJoined$1(final ParticipantJoined it, final ParticipantJoined occurrence) {
    UUID _iD = occurrence.getSource().getID();
    boolean _equals = Objects.equal(_iD, this.aid);
    return _equals;
  }

  private void $behaviorUnit$FileFound$2(final FileFound occurrence) {
    this.paths.add(occurrence.path);
  }

  private void $behaviorUnit$SearchFinished$3(final SearchFinished occurrence) {
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER.info("Holarchy destroyed.");
    Logging _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1 = this.$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LOGGING$CALLER_1.info(("Files found :" + this.paths));
    this.resultListener.onSearchCompleted(this.paths);
    Lifecycle _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER = this.$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER();
    _$CAPACITY_USE$IO_SARL_API_CORE_LIFECYCLE$CALLER.killMe();
  }

  @SyntheticMember
  @Pure
  private boolean $behaviorUnitGuard$SearchFinished$3(final SearchFinished it, final SearchFinished occurrence) {
    UUID _iD = occurrence.getSource().getID();
    boolean _equals = Objects.equal(_iD, this.aid);
    return _equals;
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

  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$Initialize(final Initialize occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$Initialize$0(occurrence));
  }

  /**
   * When the SearchFinished event is received, it means that this event was sent by the Agent that the Search Manager
   * created (condition : occurrence.source.ID == aid)
   */
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$SearchFinished(final SearchFinished occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$SearchFinished$3(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$SearchFinished$3(occurrence));
    }
  }

  /**
   * When a participant joins, it needs to be the participant created by the SearchManager,
   * that means the first Search Agent. This verification is done by verifying that the source ID
   * of the occurrence that spawned is the same as the aid of the Search Agent created by the Search Manager
   */
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$ParticipantJoined(final ParticipantJoined occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    if ($behaviorUnitGuard$ParticipantJoined$1(occurrence, occurrence)) {
      ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$ParticipantJoined$1(occurrence));
    }
  }

  /**
   * When the Search Manager receives the FileFound event, it stores the file path emitted to its List of paths
   */
  @SyntheticMember
  @PerceptGuardEvaluator
  private void $guardEvaluator$FileFound(final FileFound occurrence, final Collection<Runnable> ___SARLlocal_runnableCollection) {
    assert occurrence != null;
    assert ___SARLlocal_runnableCollection != null;
    ___SARLlocal_runnableCollection.add(() -> $behaviorUnit$FileFound$2(occurrence));
  }

  @SyntheticMember
  @Override
  public void $getSupportedEvents(final Set<Class<? extends Event>> toBeFilled) {
    super.$getSupportedEvents(toBeFilled);
    toBeFilled.add(FileFound.class);
    toBeFilled.add(SearchFinished.class);
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
    SearchManager other = (SearchManager) obj;
    if (!java.util.Objects.equals(this.criteria, other.criteria))
      return false;
    if (!java.util.Objects.equals(this.aid, other.aid))
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
    result = prime * result + java.util.Objects.hashCode(this.aid);
    return result;
  }

  @SyntheticMember
  public SearchManager(final UUID parentID, final UUID agentID) {
    super(parentID, agentID);
  }

  @SyntheticMember
  @Inject
  public SearchManager(final UUID parentID, final UUID agentID, final DynamicSkillProvider skillProvider) {
    super(parentID, agentID, skillProvider);
  }
}
