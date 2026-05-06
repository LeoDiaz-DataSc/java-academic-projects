import { useState, useRef, useCallback } from 'react';
import gsap from 'gsap';
import { useGSAP } from '@gsap/react';

gsap.registerPlugin(useGSAP);

// =============================================
// BST Node Class (Pure JavaScript implementation)
// =============================================
class BSTNode {
  constructor(value) {
    this.value = value;
    this.left = null;
    this.right = null;
  }
}

class BST {
  constructor() {
    this.root = null;
  }

  insert(value) {
    const node = new BSTNode(value);
    if (!this.root) { this.root = node; return; }
    let current = this.root;
    while (true) {
      if (value < current.value) {
        if (!current.left) { current.left = node; return; }
        current = current.left;
      } else if (value > current.value) {
        if (!current.right) { current.right = node; return; }
        current = current.right;
      } else return; // Duplicate — ignore
    }
  }

  remove(value) {
    this.root = this._removeNode(this.root, value);
  }

  _removeNode(node, value) {
    if (!node) return null;
    if (value < node.value) { node.left = this._removeNode(node.left, value); return node; }
    if (value > node.value) { node.right = this._removeNode(node.right, value); return node; }
    // Found node
    if (!node.left) return node.right;
    if (!node.right) return node.left;
    // Two children — find in-order successor
    let successor = node.right;
    while (successor.left) successor = successor.left;
    node.value = successor.value;
    node.right = this._removeNode(node.right, successor.value);
    return node;
  }

  search(value) {
    let current = this.root;
    const path = [];
    while (current) {
      path.push(current.value);
      if (value === current.value) return { found: true, path };
      current = value < current.value ? current.left : current.right;
    }
    return { found: false, path };
  }

  inOrder() { const result = []; this._inOrder(this.root, result); return result; }
  _inOrder(node, result) { if (!node) return; this._inOrder(node.left, result); result.push(node.value); this._inOrder(node.right, result); }

  preOrder() { const result = []; this._preOrder(this.root, result); return result; }
  _preOrder(node, result) { if (!node) return; result.push(node.value); this._preOrder(node.left, result); this._preOrder(node.right, result); }

  getHeight(node = this.root) {
    if (!node) return 0;
    return 1 + Math.max(this.getHeight(node.left), this.getHeight(node.right));
  }

  getNodeCount(node = this.root) {
    if (!node) return 0;
    return 1 + this.getNodeCount(node.left) + this.getNodeCount(node.right);
  }

  // Get positions for visual rendering
  getPositions() {
    const positions = [];
    const edges = [];
    this._calcPositions(this.root, 0, 0, 300, positions, edges);
    return { positions, edges };
  }

  _calcPositions(node, depth, x, spread, positions, edges) {
    if (!node) return;
    const posX = x;
    const posY = depth * 80 + 40;
    positions.push({ value: node.value, x: posX, y: posY });

    if (node.left) {
      const childX = x - spread;
      const childY = (depth + 1) * 80 + 40;
      edges.push({ x1: posX + 24, y1: posY + 24, x2: childX + 24, y2: childY + 24 });
      this._calcPositions(node.left, depth + 1, childX, spread * 0.55, positions, edges);
    }
    if (node.right) {
      const childX = x + spread;
      const childY = (depth + 1) * 80 + 40;
      edges.push({ x1: posX + 24, y1: posY + 24, x2: childX + 24, y2: childY + 24 });
      this._calcPositions(node.right, depth + 1, childX, spread * 0.55, positions, edges);
    }
  }
}

// =============================================
// BST Visualizer React Component
// =============================================
export default function BSTVisualizer() {
  const container = useRef();
  const canvasRef = useRef();
  const [tree] = useState(() => new BST());
  const [inputValue, setInputValue] = useState('');
  const [searchValue, setSearchValue] = useState('');
  const [treeData, setTreeData] = useState({ positions: [], edges: [] });
  const [stats, setStats] = useState({ height: 0, count: 0, inOrder: [], preOrder: [] });
  const [highlightedNodes, setHighlightedNodes] = useState([]);
  const [searchResult, setSearchResult] = useState(null);

  useGSAP(() => {
    gsap.from('.bst-header', { y: -30, opacity: 0, duration: 0.6, ease: 'power3.out' });
    gsap.from('.bst-controls-panel', { y: 20, opacity: 0, duration: 0.5, delay: 0.2, ease: 'power3.out' });
    gsap.from('.bst-canvas', { y: 20, opacity: 0, duration: 0.6, delay: 0.3, ease: 'power3.out' });
  }, { scope: container });

  const refreshTree = useCallback(() => {
    setTreeData(tree.getPositions());
    setStats({
      height: tree.getHeight(),
      count: tree.getNodeCount(),
      inOrder: tree.inOrder(),
      preOrder: tree.preOrder(),
    });
  }, [tree]);

  const handleInsert = () => {
    const val = parseInt(inputValue);
    if (isNaN(val)) return;
    tree.insert(val);
    refreshTree();
    setInputValue('');

    // Animate the new node
    setTimeout(() => {
      const nodes = canvasRef.current?.querySelectorAll('.bst-node');
      if (nodes) {
        const lastNode = nodes[nodes.length - 1];
        if (lastNode) {
          gsap.fromTo(lastNode, { scale: 0, opacity: 0 }, { scale: 1, opacity: 1, duration: 0.5, ease: 'back.out(1.7)' });
        }
      }
    }, 50);
  };

  const handleRemove = () => {
    const val = parseInt(inputValue);
    if (isNaN(val)) return;
    tree.remove(val);
    refreshTree();
    setInputValue('');
    setHighlightedNodes([]);
  };

  const handleSearch = () => {
    const val = parseInt(searchValue);
    if (isNaN(val)) return;
    const result = tree.search(val);
    setSearchResult(result);
    setHighlightedNodes(result.path);

    // Animate search path sequentially
    result.path.forEach((nodeVal, i) => {
      setTimeout(() => {
        const nodeEl = canvasRef.current?.querySelector(`[data-value="${nodeVal}"]`);
        if (nodeEl) {
          gsap.to(nodeEl, {
            scale: 1.2,
            duration: 0.2,
            yoyo: true,
            repeat: 1,
            ease: 'power2.inOut',
          });
        }
      }, i * 300);
    });

    setTimeout(() => { setHighlightedNodes([]); setSearchResult(null); }, result.path.length * 300 + 1500);
  };

  const handleRandomTree = () => {
    // Reset
    tree.root = null;
    const values = Array.from({ length: 10 }, () => Math.floor(Math.random() * 99) + 1);
    const unique = [...new Set(values)];
    unique.forEach(v => tree.insert(v));
    refreshTree();
    setHighlightedNodes([]);

    // Stagger animate all nodes
    setTimeout(() => {
      const nodes = canvasRef.current?.querySelectorAll('.bst-node');
      if (nodes) gsap.from(nodes, { scale: 0, opacity: 0, stagger: 0.08, duration: 0.4, ease: 'back.out(1.7)' });
      const edgeEls = canvasRef.current?.querySelectorAll('.bst-edge');
      if (edgeEls) gsap.from(edgeEls, { opacity: 0, stagger: 0.05, duration: 0.3, delay: 0.2 });
    }, 50);
  };

  const handleClear = () => {
    tree.root = null;
    refreshTree();
    setHighlightedNodes([]);
    setSearchResult(null);
  };

  // Calculate center offset for the canvas
  const centerX = 450;

  return (
    <div ref={container}>
      <div className="page-header bst-header">
        <h1 className="page-title">BST Visualizer</h1>
        <p className="page-subtitle">Árbol Binario de Búsqueda — Implementación interactiva con animaciones GSAP</p>
      </div>

      {/* Controls */}
      <div className="card bst-controls-panel" style={{ marginBottom: 20 }}>
        <div className="card-body">
          <div className="bst-controls">
            <input
              type="number"
              className="bst-input"
              placeholder="Valor..."
              value={inputValue}
              onChange={e => setInputValue(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && handleInsert()}
            />
            <button className="btn btn-primary" onClick={handleInsert}>Insertar</button>
            <button className="btn btn-secondary" onClick={handleRemove}>Eliminar</button>

            <div style={{ width: 1, height: 32, background: 'var(--color-border)', margin: '0 8px' }} />

            <input
              type="number"
              className="bst-input"
              placeholder="Buscar..."
              value={searchValue}
              onChange={e => setSearchValue(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && handleSearch()}
            />
            <button className="btn btn-secondary" onClick={handleSearch}>🔍 Buscar</button>

            <div style={{ width: 1, height: 32, background: 'var(--color-border)', margin: '0 8px' }} />

            <button className="btn btn-primary" onClick={handleRandomTree}>🎲 Árbol Aleatorio</button>
            <button className="btn btn-secondary" onClick={handleClear}>🗑️ Limpiar</button>
          </div>

          {searchResult && (
            <div style={{ marginTop: 12, fontSize: '0.875rem' }}>
              {searchResult.found
                ? <span style={{ color: 'var(--color-accent-success)' }}>✅ Valor encontrado — Camino: [{searchResult.path.join(' → ')}]</span>
                : <span style={{ color: 'var(--color-accent-danger)' }}>❌ Valor no encontrado — Camino recorrido: [{searchResult.path.join(' → ')}]</span>
              }
            </div>
          )}
        </div>
      </div>

      {/* Tree Canvas */}
      <div className="bst-canvas" ref={canvasRef} style={{ minHeight: Math.max(400, stats.height * 100 + 100) }}>
        {treeData.positions.length === 0 ? (
          <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', height: '100%', minHeight: 400, flexDirection: 'column', gap: 12, color: 'var(--color-text-muted)' }}>
            <span style={{ fontSize: '3rem' }}>🌳</span>
            <p>Inserta un valor o genera un árbol aleatorio para comenzar</p>
          </div>
        ) : (
          <>
            {/* Edges */}
            {treeData.edges.map((edge, i) => {
              const dx = (edge.x2 + centerX) - (edge.x1 + centerX);
              const dy = edge.y2 - edge.y1;
              const length = Math.sqrt(dx * dx + dy * dy);
              const angle = Math.atan2(dy, dx) * (180 / Math.PI);
              return (
                <div
                  key={`edge-${i}`}
                  className="bst-edge"
                  style={{
                    left: edge.x1 + centerX,
                    top: edge.y1,
                    width: length,
                    transform: `rotate(${angle}deg)`,
                  }}
                />
              );
            })}
            {/* Nodes */}
            {treeData.positions.map((node, i) => (
              <div
                key={`node-${node.value}-${i}`}
                className={`bst-node ${highlightedNodes.includes(node.value) ? 'highlight' : ''}`}
                data-value={node.value}
                style={{ left: node.x + centerX, top: node.y }}
              >
                {node.value}
              </div>
            ))}
          </>
        )}
      </div>

      {/* Stats */}
      {stats.count > 0 && (
        <div className="bst-info" style={{ marginTop: 16 }}>
          <div className="bst-info-item"><strong>Nodos:</strong> {stats.count}</div>
          <div className="bst-info-item"><strong>Altura:</strong> {stats.height}</div>
          <div className="bst-info-item"><strong>In-Order:</strong> [{stats.inOrder.join(', ')}]</div>
          <div className="bst-info-item"><strong>Pre-Order:</strong> [{stats.preOrder.join(', ')}]</div>
        </div>
      )}
    </div>
  );
}
