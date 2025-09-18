import { reactive } from 'vue';

type MessageHandler = (event: MessageEvent) => void;

class WebSocketService {
  private socket: WebSocket | null = null;
  private handlers: Set<MessageHandler> = new Set();
  public state = reactive({ connected: false });

  connect(url = (import.meta.env.VITE_WS_URL as string) || 'ws://localhost:8080/ws/routes'): void {
    if (this.socket) return;
    try {
      this.socket = new WebSocket(url);
      this.socket.onopen = () => {
        this.state.connected = true;
      };
      this.socket.onclose = () => {
        this.state.connected = false;
        this.socket = null;
        // naive reconnect
        setTimeout(() => this.connect(url), 2000);
      };
      this.socket.onmessage = (e) => {
        this.handlers.forEach((h) => h(e));
      };
      this.socket.onerror = () => {
        // let close trigger reconnect
      };
    } catch {
      // ignore
    }
  }

  subscribe(handler: MessageHandler): () => void {
    this.handlers.add(handler);
    return () => this.handlers.delete(handler);
  }
}

export const wsService = new WebSocketService();


